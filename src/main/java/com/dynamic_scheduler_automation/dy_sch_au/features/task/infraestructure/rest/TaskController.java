package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.rest;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.application.TaskUseCase;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.NotSuchTaskException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskUseCase useCase;

    public TaskController(TaskUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public Page<Task> listAll(@PageableDefault(size = 10) Pageable pageable) {
        return useCase.listTasks(pageable);
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable String id) {
        return useCase.getTaskById(id).orElseThrow(() -> new NotSuchTaskException(id));
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        return new ResponseEntity<Task>(useCase.createTask(task), HttpStatus.CREATED);
    }

}
