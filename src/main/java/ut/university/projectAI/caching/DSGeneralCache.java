package ut.university.projectAI.caching;

public interface DSGeneralCache<K, V> {
	void insert(K key, V value);
	V getCached(K key);
	void preCache();
}
