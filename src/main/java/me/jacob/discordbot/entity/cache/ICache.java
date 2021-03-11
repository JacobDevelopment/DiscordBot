package me.jacob.discordbot.entity.cache;

public interface ICache<K, V> {

    void put(K key, V value);

    void delete(K key);

    void update(K key, V updatedValue);

    V getById(K key);

    V retrieveById(K key);

}
