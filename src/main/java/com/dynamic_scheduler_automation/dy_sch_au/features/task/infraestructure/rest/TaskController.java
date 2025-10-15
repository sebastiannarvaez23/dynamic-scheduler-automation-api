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
    public Page<Task> listAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String cronExpression,
            @RequestParam(required = false) Boolean active
    ) {
        return useCase.listTasks(pageable, code, name, description, cronExpression, active);
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable String id) {
        return useCase.getTaskById(id).orElseThrow(() -> new NotSuchTaskException(id));
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        return new ResponseEntity<Task>(useCase.createTask(task), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
            @PathVariable String id,
            @Valid @RequestBody Task task
    ) {
        return ResponseEntity.ok(useCase.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        useCase.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
