package com.kingston.jforgame.admin.gamecmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class GameCmdService {

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        String url = "http://localhost:3307/serverController/hotSwap";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        HttpStatus statusCode = result.getStatusCode();
        String body = result.getBody();
    }

}
