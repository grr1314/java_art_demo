package cp5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

/**
 * Mutex是一个独占锁，帮助理解同步器（所谓的独占锁就是就有一个线程能获得锁） 问题1
 * AbstractQueuedSynchronizer（同步器）内部的数据结构是什么样的 问题2 怎么实现线程安全的 问题3 lock之后流程是什么样的？
 * <p>
 * 一 lock 流程 1 lock方法调用同步器的acquire方法 2 acquire方法调用tryAcquire方法 3
 * tryAcquire方法调用compareAndSetState(0, 1)，当前线程尝试获取锁， 如果compareAndSetState(0,
 * 1)返回true则调用 setExclusiveOwnerThread(Thread.currentThread());表示当前线程获取到了锁，其他
 * 线程只能等着了，此时tryAcquire方法返回true，否则tryAcquire方法返回false 4
 * 如果tryAcquire返回的是false，则调用acquireQueued方法将当前线程的节点放在队尾 5
 * 当前调用selfInterrupt方法将自己中断，selfInterrupt方法中调用了Thread.currentThread().interrupt();来中断自己
 * <p>
 * 
 * 
 * 
 * 
 * 二 tryRelease 1 在tryRelease方法中先判断getState() 是否等于0，如果等于0的话表示当前线程没有获得锁，谈何释放？ 2
 * 如果getState不等于0（其实等于1）的时候表示当前线程获得了锁，则调用 setExclusiveOwnerThread(null);释放锁 3 调用
 * setState(0);将state的值恢复为0
 * <p>
 * tryRelease流程很简单。。。。
 * <p>
 * 
 * 所以说判断锁是否被持有，其实就是判断Lock内部维护的这个state是否等于1，等于1就表示被某个线程持有。 问题1：队列FIFO的
 * 问题2：通过维护一个state值来判断线程是否获取到了锁，多个线程使用cas算法去竞争锁，没有竞争到锁的线程就被放到了同步队列里面 问题3：上面已经回答了
 * <p>
 * 现在源码看到这里我又有一个疑问，问题4：在同队列里面等着的线程怎么重新被唤起，之前看到进入同步队列中的线程是被中断了的！！！
 * 
 * 三 unlock 流程
 * unlock方法会调用sync的release方法，然后release方法会调用tryRelease。tryRelease上面分析过了，如果返回的是true表示
 * 释放成功，然后获取同步器的头节点，并通过unparkSuccessor唤醒头节点的线程。 public final boolean release(int
 * arg) { if (tryRelease(arg)) { Node h = head; if (h != null && h.waitStatus !=
 * 0) unparkSuccessor(h); return true; } return false; }，然后unparkSuccessor底层是调用了LockSupport.unpark(Thread)
 * 方法来真的实现线程唤起操作
 * 
 * 
 * 
 */

public class Mutex {
    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                // 这个方法作用是设置当前拥有锁的线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0)
                throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    private Sync sync = new Sync();

    public void unlock() {
        sync.release(1);
    }

    public void lock() {
        sync.acquire(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public void tryLock(long time, TimeUnit unit) throws InterruptedException {
        sync.tryAcquireNanos(1, unit.toNanos(time));
    }
}
