package ibf.ssf.todoapp.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static ibf.ssf.todoapp.Constants.*;
import ibf.ssf.todoapp.service.TaskService;

@Controller
@RequestMapping(
    path="/task",
    produces=MediaType.TEXT_HTML_VALUE)
public class TaskController {
    private final Logger logger = Logger.getLogger(TaskController.class.getName());
    @Autowired
    TaskService taskService;


    @GetMapping
    public String getTasks() {
        return "tasks";
    }

    // @GetMapping("{taskId}")
    // public String getTask() {
    //     return "";
    // }

    @PostMapping
    public String submitForm(@RequestBody MultiValueMap<String, String> form, Model model) {
        String userName = form.getFirst("userName");
        String taskName = form.getFirst("taskName");
        String contents = form.getFirst("contents");
        Optional<String> userNameOpt = Optional.ofNullable(userName);
        Optional<String> taskNameOpt = Optional.ofNullable(taskName);
        Optional<String> contentsOpt = Optional.ofNullable(contents);
        logger.log(Level.INFO, "form body: '%s'".formatted(form));
        logger.log(Level.INFO, "form contents: '%s'".formatted(contents));
        logger.log(Level.INFO, "using delimiter: '%s'".formatted(FORM_DELIMITER));

        String cleanedUserName = userNameOpt.map(String::trim).orElse("");
        String cleanedTaskName = taskNameOpt.map(String::trim).orElse("");

        logger.log(Level.INFO, "userName: %s".formatted(cleanedUserName));

        List<String> tasks = new ArrayList<>();
        if (contentsOpt.map(String::trim).filter(e -> e.length() > 0).isPresent()) {
            // add new task to contents
            if (cleanedTaskName.length() > 0) {
                contents += FORM_DELIMITER + cleanedTaskName;
                logger.log(Level.INFO, "updated contents: '%s'".formatted(contents));
            }
            tasks = Arrays.asList(contents.split(Pattern.quote(FORM_DELIMITER)));
        } else {
            // contents is empty
            if (cleanedTaskName.length() > 0) {
                contents = cleanedTaskName;
                logger.log(Level.INFO, "initialising contents: '%s'".formatted(contents));
                tasks.add(taskName);
            }
        }

        logger.log(Level.INFO, "taskName: %s".formatted(taskName));

        model.addAttribute("userName", cleanedUserName);
        model.addAttribute("contents", contents);
        model.addAttribute("tasks", tasks);

        return "tasks";
    }

    @PostMapping("save")
    public String saveTasks(@RequestBody MultiValueMap<String, String> form, Model model) {
        String userName = form.getFirst("userName");
        String contents = form.getFirst("contents");
        Optional<String> userNameOpt = Optional.ofNullable(userName);
        Optional<String> contentsOpt = Optional.ofNullable(contents);

        String cleanedUserName = userNameOpt
                                    .map(String::trim)
                                    .orElse("");

        logger.log(Level.INFO, "To save: %s under key: %s".formatted(contents, cleanedUserName));
        List<String> tasks = new ArrayList<>();
        contentsOpt = contentsOpt.map(String::trim).filter(e -> e.length() > 0);
        if (contentsOpt.isPresent()) {
            tasks = Arrays.asList(contentsOpt.get().split(Pattern.quote(FORM_DELIMITER)));
        }
        taskService.save(cleanedUserName, tasks);

        model.addAttribute("userName", cleanedUserName);
        model.addAttribute("contents", contents);
        model.addAttribute("tasks", tasks);

        return "tasks";
    }


}
