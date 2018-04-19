package org.activiti.myExplorer.cache;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisCacheManger implements RedisCache {
	private ShardedJedisPool pool;

	public ShardedJedisPool getPool() {
		return pool;
	}

	public void setPool(ShardedJedisPool pool) {
		this.pool = pool;
	}

	public <T> T getRedisCacheInfo(String key) {

		try {
			ShardedJedis jedis = pool.getResource();
			pool.returnResource(jedis);
			return (T) jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> boolean setRedisCacheInfo(String key, T value) {

		try {
			ShardedJedis jedis = pool.getResource();
			jedis.set(key, (String) value);
			pool.returnResource(jedis);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		new RedisCacheManger().setRedisCacheInfo("12345", "asdfg");
	}
}
