package cp5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition
 * <p>
 * 一 什么是Condition
 * 任意一个Java对象，都拥有一组监视器方法，主要包括wait()、wait(long timeout)、notify()、notifyAll()，他们和synchronized关键字
 * 配合来实现线程的等待\通知模式，同样的Condition接口也提供了一套类似的方法实现等待\通知模式，只不过它是配合Lock一起使用的。
 *
 * 二 有什么关键方法
 * await()
 * await(long time,TimeUnit unit)
 * ...
 *
 * signal()
 * signalAll()
 *
 */

public class ConditionDemo {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void conditionWait() {
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void conditionSignal() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

}
