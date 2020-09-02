import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对比HashMap来看
 * 问题1 HashMap为什么不能支持并发？
 * 1.7版本的HashMap put数据的时候会成环
 * 1.8虽说不会成环形，但是有一定概率会覆盖数据
 * ps：HashMap扩容的是时候元素不会再一次hash
 * 问题2 HashMap不是线程安全的那么有什么线程安全的Map类数据结构？HashTable、ConcurrentHashMap
 * HashTable之所以是线程安全的是因为它的关键方法都加上了锁，竞争很激烈效率很低至于所相关可以复习前面章节的内容
 * 然后就是ConcurrentHashMap它的实现方案就和HashTable不一样了。ConcurrentHashMap Java7和8还是存在一些不同的
 * 书中是基于7将的，这里直接尝试看8中的ConcurrentHashMap源码。正式看之前带着几个问题去看
 * 问题1：ConcurrentHashMap如何实现线程安全的
 * 问题2 ConcurrentHashMap如何确定大小？是否有类似于HashMap的扩容机制？
 * 问题3 相比于HashTable性能优越在哪里？
 * let's go
 *
 * 一 初始化
 *
 * 1 默认初始化
 *
 *       Creates a new, empty map with the default initial table size (16).
 *
 *       public ConcurrentHashMap(){
 *
 *       }
 *
 *  很简单什么操作都没有，注释也写的很清楚，创建一个空的大小为16的table（数组）
 *
 *
 * 2 put方法
 *
 * final V putVal(K key, V value, boolean onlyIfAbsent) {
 *         if (key == null || value == null) throw new NullPointerException(); //（1）说明key和value都不能为空
 *         int hash = spread(key.hashCode()); //（2）计算key的hash值
 *         int binCount = 0;//（3）记录链表长度的变量，当binCount>8之后就会变成红黑树
 *         for (Node<K,V>[] tab = table;;) {
 *             Node<K,V> f; int n, i, fh;
 *             if (tab == null || (n = tab.length) == 0) //（4）如果说数组为空的话或者数组长度==0，就初始化数组
 *                 tab = initTable();（5）初始化数组
 *             else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {//（6）根据put的元素打的key的hash值来计算下标，并判断该位置是否已经有元素了，其中下标的算法和hashmap的一样
 *             //都是数组大小-1按位与hash
 *                 if (casTabAt(tab, i, null,
 *                              new Node<K,V>(hash, key, value, null))) （7）数组桶数据为空的话，使用cas替换，是线程安全的
 *                     break;                   // no lock when adding to empty bin
 *             }
 *             else if ((fh = f.hash) == MOVED)
 *                 tab = helpTransfer(tab, f);（8）扩容数组
 *             else {
 *                 V oldVal = null;
 *                 synchronized (f) {（8）使用锁
 *                     if (tabAt(tab, i) == f) {
 *                         if (fh >= 0) {
 *                             binCount = 1;
 *                             for (Node<K,V> e = f;; ++binCount) {
 *                                 K ek;
 *                                 if (e.hash == hash &&
 *                                     ((ek = e.key) == key ||
 *                                      (ek != null && key.equals(ek)))) {
 *                                     oldVal = e.val;
 *                                     if (!onlyIfAbsent)（9）在链表上比较元素如果发现是一样的就替换
 *                                         e.val = value;
 *                                     break;
 *                                 }
 *                                 Node<K,V> pred = e;
 *                                 if ((e = e.next) == null) {（10）如果发现不一样就往下一个位置放元素
 *                                     pred.next = new Node<K,V>(hash, key,
 *                                                               value, null);
 *                                     break;
 *                                 }
 *                             }
 *                         }
 *                         else if (f instanceof TreeBin) {
 *                             Node<K,V> p;
 *                             binCount = 2;
 *                             if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
 *                                                            value)) != null) {
 *                                 oldVal = p.val;
 *                                 if (!onlyIfAbsent)
 *                                     p.val = value;
 *                             }
 *                         }
 *                     }
 *                 }
 *                 if (binCount != 0) {
 *                     if (binCount >= TREEIFY_THRESHOLD)
 *                         treeifyBin(tab, i);
 *                     if (oldVal != null)
 *                         return oldVal;
 *                     break;
 *                 }
 *             }
 *         }
 *         addCount(1L, binCount);
 *         return null;
 *     }
 *
 * 看完put方法我们知道了ConcurrentHashMap的数据结构是和HashMap一样的，它在put的时候利用了cas和锁两种形式保证了线程安全，
 * 在出现hash冲突的时候才会使用锁去保证线程安全，相比HashTable的方式性能要提升了不少。关于问题2我们还要接着看helpTransfer
 * 方法
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class ConcurrentHashMapDemo {
    HashMap<String,String> hashMap=new HashMap<>();
    ConcurrentHashMap<String,String> con= new ConcurrentHashMap<>();//默认初始化，什么参数都没有
    public static void main(String[] args) {
        ConcurrentHashMapDemo concurrentHashMapDemo=new ConcurrentHashMapDemo();
        concurrentHashMapDemo.put("key", "value");
//        concurrentHashMapDemo.hashMap.put(key, value)
//        concurrentHashMapDemo.hashMap.remove(key)

    }
    public void put(String key,String value)
    {
        con.put(key, value);
    }
    public String get(String key)
    {
        return con.get(key);
    }
}