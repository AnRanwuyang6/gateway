package com.zongk.online.getway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zongk.online.getway.common.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件名
 * Created at 2020/8/20
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@Component
@ConfigurationProperties("org.my.jwt")
@Getter
@Setter
public class JwtTokenFilter implements GlobalFilter,Ordered {

    private ObjectMapper objectMapper;

    private String[] skipAuthUrls;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();

        //跳过不需要验证的路径
        if(null != skipAuthUrls&& Arrays.asList(skipAuthUrls).contains(url)){
            return chain.filter(exchange);
        }
        //获取token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        ServerHttpResponse resp = exchange.getResponse();
        if(StringUtils.isBlank(token)){
            //没有token
            return authErro(resp,"请登陆");
        }else{
            //有token
            try {
                JwtUtil.checkToken(token,objectMapper);
                return chain.filter(exchange);
            }catch (ExpiredJwtException e){
                //log.error(e.getMessage(),e);
                if(e.getMessage().contains("Allowed clock skew")){
                    return authErro(resp,"认证过期");
                }else{
                    return authErro(resp,"认证失败");
                }
            }catch (Exception e) {
                //log.error(e.getMessage(),e);
                return authErro(resp,"认证失败");
            }
        }
    }


    /**
     * 认证错误输出
     *
     * @param resp 响应对象
     * @param mess 错误信息
     * @return
     */
    private Mono<Void> authErro(ServerHttpResponse resp, String mess) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        Map<String,Object> returnData=new HashMap<>();
        returnData.put("status",HttpStatus.UNAUTHORIZED);
        returnData.put("message",mess);
        //Result returnData = new Result().setCode(org.apache.http.HttpStatus.SC_UNAUTHORIZED).setMsg(mess);
        String returnStr = "";
        try {
            returnStr = objectMapper.writeValueAsString(returnData);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }


    @Override
    public int getOrder() {
        return -100;
    }
}
