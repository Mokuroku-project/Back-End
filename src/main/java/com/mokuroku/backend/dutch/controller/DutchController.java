package com.mokuroku.backend.dutch.controller;

import com.mokuroku.backend.dutch.dto.DutchDTO;
import com.mokuroku.backend.dutch.service.DutchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DutchController {

    private final DutchService dutchService;

    public DutchController(DutchService dutchService){
        this.dutchService = dutchService;
    }

    @PostMapping("/dutch/")
    public Map<String, Integer> dutch(@RequestBody DutchDTO.DutchRequest request){
    Map<String, Integer> dutchResult = dutchService.calculate(request);
    return dutchResult;
    }

}
