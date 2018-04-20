package org.activiti.account.cache;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

	private final Charset charset;

	public FastJsonRedisSerializer() {
		this(Charset.forName("UTF8"));
	}

	public FastJsonRedisSerializer(Charset charset) {
		Assert.notNull(charset);
		this.charset = charset;
	}

	static boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}

	@Override
	public byte[] serialize(Object source) throws SerializationException {
		if (source == null) {
			return new byte[0];
		}
		return JSON.toJSONBytes(source, SerializerFeature.WriteClassName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (isEmpty(bytes)) {
			return null;
		}
		return (T) JSON.parse(new String(bytes, charset), Feature.SupportAutoType);
	}
}
