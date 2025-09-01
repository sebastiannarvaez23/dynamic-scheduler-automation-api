package com.dynamic_scheduler_automation.dy_sch_au.task.infraestructure.rest;

import com.dynamic_scheduler_automation.dy_sch_au.task.application.TaskUseCase;
import com.dynamic_scheduler_automation.dy_sch_au.task.domain.model.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskUseCase useCase;

    public TaskController(TaskUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public List<Task> listAll() {
        return useCase.listTasks();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return useCase.getTaskById(id).orElseThrow();
    }

    @PostMapping
    public Task create(@RequestBody Task task) {
        return useCase.createTask(task);
    }
}
