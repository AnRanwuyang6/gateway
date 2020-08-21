package com.zongk.online.getway.controller;

import com.zongk.online.getway.common.JwtUtil;
import com.zongk.online.getway.common.UserDTO;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件名
 * Created at 2020/8/20
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@RestController
@ConfigurationProperties("org.my.jwt")
@RequestMapping("/auth")
public class LoginController {

    private String[] skipAuthUrls;

    @Value("${org.my.jwt.effective-time:1m}")
    private String effectiveTime;

    /**
     * 登陆认证接口
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody UserDTO userDTO) throws Exception {
        ArrayList<String> roleIdList = new ArrayList<>(1);
        roleIdList.add("role_test_1");
        //JwtModel jwtModel = new JwtModel("test", roleIdList);
        int effectivTimeInt = Integer.valueOf(effectiveTime.substring(0,effectiveTime.length()-1));
        String effectivTimeUnit = effectiveTime.substring(effectiveTime.length()-1,effectiveTime.length());
        String jwt = null;
        switch (effectivTimeUnit){
            case "s" :{
                //秒
                jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), userDTO.getUser(), effectivTimeInt * 1000L);
                break;
            }
            case "m" :{
                //分钟
                jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), userDTO.getUser(), effectivTimeInt * 60L * 1000L);
                break;
            }
            case "h" :{
                //小时
                jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), userDTO.getUser(), effectivTimeInt * 60L * 60L * 1000L);
                break;
            }
            case "d" :{
                //小时
                jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), userDTO.getUser(),  effectivTimeInt * 24L * 60L * 60L * 1000L);
                break;
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("jwt",jwt);
        map.put("status","success");
        return map;
    }
}
