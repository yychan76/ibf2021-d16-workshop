package ibf.ssf.todoapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf.ssf.todoapp.repositories.TaskRepository;

@Service
public class TaskService {
    private static final Logger logger = Logger.getLogger(TaskService.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TaskRepository taskRepository;

    public boolean hasKey(String key) {
        return taskRepository.existsById(key);
    }

    public void save(String key, List<String> values) {
        logger.info("Saving %s under key %s".formatted(values, key));
        taskRepository.save(key, values);
    }

    public List<String> getItems(String key) {
        Optional<Object> valuesOpt = taskRepository.findById(key);
        if (valuesOpt.isPresent()) {
            return mapper.convertValue(valuesOpt.get(), new TypeReference<List<String>>() {});
        } else {
            return new ArrayList<>();
        }

    }
}
