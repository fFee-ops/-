package com.java_lettuce.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisTemplate-->redis进行了进一步封装 （lettuce)
 */

@Configuration
public class redisConfig {
    /**
     * 在redisTemplate()这个方法中用JacksonJsonRedisSerializer更换掉了Redis默认的序列化方式：JdkSerializationRedisSerializer
     *JdkSerializationRedisSerializer序列化被序列化对象必须实现Serializable接口，被序列化除属性内容还有其他
     * 内容，长度长且不易阅读,默认就是采用这种序列化方式
     *
     *
     *JacksonJsonRedisSerializer序列化,被序列化对象不需要实现Serializable接口，被序列化的结果清晰，容易阅
     * 读，而且存储字节少，速度快
     *一般如果key、value都是string字符串的话，就是用这个就可以了
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory factory){
        RedisTemplate<String,Object> template = new RedisTemplate <>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
                Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
// 在使用注解@Bean返回RedisTemplate的时候，同时配置hashKey与hashValue的序列化方式。
// key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
// value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
// hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
// hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
