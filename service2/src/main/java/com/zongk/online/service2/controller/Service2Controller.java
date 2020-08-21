package com.zongk.online.service2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件名
 * Created at 2020/8/19
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@RestController
@RequestMapping("/test")
public class Service2Controller {
    @RequestMapping("/info")
    public Map<String,Object> test(){
        Map<String,Object> map=new HashMap<>();
        map.put("service","service2");
        map.put("username","lizk");
        map.put("sex","男");
        map.put("age","24");
        return map;
    }
}
