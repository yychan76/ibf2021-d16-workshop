package ibf.ssf.todoapp.repositories;

import java.util.Optional;


public interface RedisRepo {
    public void save(String key, Object value);
    public Optional<Object> findById(String key);
    public boolean existsById(String key);
}
