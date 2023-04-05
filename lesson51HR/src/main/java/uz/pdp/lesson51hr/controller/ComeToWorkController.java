package uz.pdp.lesson51hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson51hr.entity.ComeToWork;
import uz.pdp.lesson51hr.payload.ApiResponse;
import uz.pdp.lesson51hr.payload.ComeToWorkDto;
import uz.pdp.lesson51hr.payload.TwoTimePeriodsDto;
import uz.pdp.lesson51hr.service.ComeToWorkService;

import java.util.List;

@RestController
@RequestMapping("/api/comeToWork")
public class ComeToWorkController {
    @Autowired
    ComeToWorkService comeToWorkService;

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getComeToWorks(){
        List<ComeToWork> comeToWorks = comeToWorkService.getComeToWorks();
        return ResponseEntity.ok(comeToWorks);
    }

    @PostMapping
    public HttpEntity<?> addComeToWork(@RequestBody ComeToWorkDto comeToWorkDto){
        ApiResponse apiResponse = comeToWorkService.addComeToWork(comeToWorkDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping("/twoTimePeriods/{username}")
    public HttpEntity<?> getTwoTimePeriods(@PathVariable String username, @RequestBody TwoTimePeriodsDto twoTimePeriodsDto){
        List<String> str = comeToWorkService.getTwoTimePeriods(username, twoTimePeriodsDto);
        return ResponseEntity.ok(str);
    }

}
