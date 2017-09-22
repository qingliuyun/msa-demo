package com.digitalchina.platform.service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/calculator")
public class CalculatorController {

    @RequestMapping(value = "/plus.do", method = RequestMethod.GET)
    public Double plus(@RequestParam double first, @RequestParam double second) {
        return first+second;
    }

}
