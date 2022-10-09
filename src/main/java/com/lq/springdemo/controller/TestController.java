package com.lq.springdemo.controller;


import com.alibaba.fastjson.JSONObject;
import com.lq.springdemo.entity.Actor;
import com.lq.springdemo.entity.common.BasePageResult;
import com.lq.springdemo.entity.common.ResultView;
import com.lq.springdemo.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@RequestMapping("/Test")
public class TestController {

    @Autowired
    private ActorService actorService;

    @GetMapping("/hello")
    public String hello(){
        return "HELLO";
    }


    @GetMapping("/getActor")
    public ResultView<Actor> selectByPrimaryKey(long key){
        Actor actor = actorService.selectByPrimaryKey(key);

        return new ResultView<Actor>(actor);
    }

    @GetMapping("/page")
    public BasePageResult<List<Actor>> getPageData(Actor actor){
        BasePageResult<List<Actor>> actors = actorService.getPageData(actor);
        return actors;
    }
}
