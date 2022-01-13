package ibf.ssf.todoapp.repositories;

import static ibf.ssf.todoapp.Constants.*;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository implements RedisRepo {
    private static final Logger logger = Logger.getLogger(TaskRepository.class.getName());

    @Autowired
    private RedisTemplate<String, Object> template;

    @Override
    public void save(String key, Object value) {
        // enforce key lower case
        key = key.toLowerCase();
        // template.opsForValue().set(key, value, Constants.ITEMS_STORE_LIFETIME_MINUTES, TimeUnit.MINUTES);
        template.opsForHash().put(TODO_ITEMS_KEY, key, value);
    }

    @Override
    public Optional<Object> findById(String key) {
        // enforce key lower case
        key = key.toLowerCase();
        logger.info("findById: %s hash: %s key: %s".formatted(template.opsForHash().get(TODO_ITEMS_KEY, key), TODO_ITEMS_KEY, key));
        // return Optional.ofNullable(template.opsForValue().get(key));
        return Optional.ofNullable(template.opsForHash().get(TODO_ITEMS_KEY, key));
    }

    @Override
    public boolean existsById(String key) {
        // enforce key lower case
        key = key.toLowerCase();
        return template.opsForHash().hasKey(TODO_ITEMS_KEY, key);
    }
}
