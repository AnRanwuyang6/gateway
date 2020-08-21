package com.zongk.online.service1.controller;

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
public class Service1Controller {
    @RequestMapping("/info")
    public Map<String,Object> test(){
        Map<String,Object> map=new HashMap<>();
        map.put("username","东方不败");
        map.put("sex","女");
        map.put("age","24");
        return map;
    }
}
