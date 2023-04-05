package uz.pdp.lesson51hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson51hr.entity.GoFromWork;
import uz.pdp.lesson51hr.payload.ApiResponse;
import uz.pdp.lesson51hr.payload.GoFromWorkDto;
import uz.pdp.lesson51hr.service.GoFromWorkService;

import java.util.List;

@RestController
@RequestMapping("/api/goFromWork")
public class GoFromWorkController {
    @Autowired
    GoFromWorkService goFromWorkService;

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getGoFromWorks(){
       List<GoFromWork> goFromWorks = goFromWorkService.getGoFromWorks();
       return ResponseEntity.ok(goFromWorks);
    }

    @PostMapping
    public HttpEntity<?> addGoFromWork(@RequestBody GoFromWorkDto goFromWorkDto){
        ApiResponse apiResponse = goFromWorkService.addGoFromWork(goFromWorkDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

}
