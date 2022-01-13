package ibf.ssf.todoapp.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration

public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;

    @Value("${spring.redis.database}")
    private int redisDatabase;

    @Value("${spring.redis.password}")
    private Optional<String> redisPassword;

    @Bean
    @Scope("singleton")
    public RedisTemplate<String, Object> createRedisTemplate() {
        final RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(redisHost);
        standaloneConfig.setPort(redisPort.get());
        standaloneConfig.setDatabase(redisDatabase);
        if (redisPassword.isPresent()) {
            standaloneConfig.setPassword(redisPassword.get());
        }

        final JedisClientConfiguration jedisClientConfig = JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFactory = new JedisConnectionFactory(standaloneConfig, jedisClientConfig);
        logger.info("redis host: {} port: {}", redisHost, redisPort.get());
        jedisFactory.afterPropertiesSet();

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }
}
