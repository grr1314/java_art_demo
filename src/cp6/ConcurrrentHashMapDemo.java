package cp6;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 第六章 并发容器和框架
 */
public class ConcurrrentHashMapDemo {
    private ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();

    private void add() {
        concurrentHashMap.put("key", "value");
    }

}
