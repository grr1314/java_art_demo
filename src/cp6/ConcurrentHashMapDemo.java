import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对比HashMap来看
 * 问题1 HashMap为什么不能支持并发？
 * 1.7版本的HashMap put数据的时候会成环
 * 1.8虽说不会成环形，但是有一定概率会覆盖数据
 * ps：HashMap扩容的是时候元素不会再一次hash
 * 问题2 HashMap不是线程安全的那么有什么线程安全的Map类数据结构？HashTable、ConcurrentHashMap
 * HashTable之所以是线程安全的是因为它的关键方法都加上了锁，竞争很激烈效率很低。然后就是ConcurrentHashMap
 * 它的实现方案就和HashTable不一样了。
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
 * 
 * 
 * 
 */
public class ConcurrentHashMapDemo {
    HashMap<String,String> hashMap=new HashMap<>();
    ConcurrentHashMap<String,String> con= new ConcurrentHashMap<>();
    public static void main(String[] args) {
        ConcurrentHashMapDemo concurrentHashMapDemo=new ConcurrentHashMapDemo();
        concurrentHashMapDemo.put("key", "value");
        concurrentHashMapDemo.hashMap.put(key, value)
        concurrentHashMapDemo.hashMap.remove(key)
        
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