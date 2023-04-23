package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @RequestMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
