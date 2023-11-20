package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: rocketmq
 * @package: com.example.controller
 * @className: qpsController
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/17 17:39
 * @version: 1.0
 */
@RestController
public class qpsController {

    @GetMapping("/test")
    public String test(){
        return "ok";
    }
    @GetMapping("/test2")
    public String test2(){
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }


}
