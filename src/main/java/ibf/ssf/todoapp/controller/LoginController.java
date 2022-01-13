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
    path="/",
    produces=MediaType.TEXT_HTML_VALUE)
public class LoginController {
    private final Logger logger = Logger.getLogger(LoginController.class.getName());
    @Autowired
    TaskService taskService;


    @GetMapping
    public String getTasks() {
        return "login";
    }

    // @GetMapping("{taskId}")
    // public String getTask() {
    //     return "";
    // }

    @PostMapping
    public String submitForm(@RequestBody MultiValueMap<String, String> form, Model model) {
        String userName = form.getFirst("userName");
        Optional<String> userNameOpt = Optional.ofNullable(userName);
        logger.log(Level.INFO, "form body: '%s'".formatted(form));

        String cleanedUserName = userNameOpt
                                    .map(String::trim)
                                    .orElse("");

        logger.log(Level.INFO, "userName: %s".formatted(cleanedUserName));

        List<String> retrievedTasks = taskService.getItems(cleanedUserName);
        logger.log(Level.INFO, "retrieved tasks: %s".formatted(retrievedTasks));

        String contents = String.join(FORM_DELIMITER, retrievedTasks);
        logger.log(Level.INFO, "retrieved contents: %s".formatted(contents));

        model.addAttribute("userName", cleanedUserName);
        model.addAttribute("tasks", retrievedTasks);
        model.addAttribute("contents", contents);

        return "tasks";
    }




}
