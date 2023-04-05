package uz.pdp.lesson51hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson51hr.entity.Task;
import uz.pdp.lesson51hr.payload.ApiResponse;
import uz.pdp.lesson51hr.payload.TaskDto;
import uz.pdp.lesson51hr.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getAllTasks(){
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public HttpEntity<?> getTask(@PathVariable Integer id){
        Task task = taskService.getTask(id);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @PostMapping
    public HttpEntity<?> addTask(@RequestBody TaskDto taskDto){
        ApiResponse apiResponse = taskService.addTask(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @PutMapping("/directorOrManager/{id}")
    public HttpEntity<?> editTaskByManager(@PathVariable Integer id, @RequestBody TaskDto taskDto){
        ApiResponse apiResponse = taskService.editByManager(id, taskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @PutMapping("/user/{id}")
    public HttpEntity<?> editTaskByUser(@PathVariable Integer id, @RequestParam boolean done){
        ApiResponse apiResponse = taskService.editByUser(id, done);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/verifyTask")
    public HttpEntity<?> verifyTask(@RequestParam String email, @RequestParam Integer taskId){
        ApiResponse apiResponse = taskService.verifyTask(email, taskId);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

}
