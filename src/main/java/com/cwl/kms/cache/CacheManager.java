package com.cwl.kms.cache;

import java.util.Map;

/**
 * ClassName: CacheManager
 * Package: com.cwl.kms.cache
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:59
 * @Version 1.0
 */
public interface CacheManager<K, V> {

    V get(K key);

    void put(K key, V value);

    void putAll(Map<K,V> cacheMap);

    void refresh(K key);

    void initializeCache();
}
