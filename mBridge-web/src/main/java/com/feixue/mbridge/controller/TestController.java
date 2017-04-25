package com.feixue.mbridge.controller;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.domain.protocol.ProtocolPath;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 15/10/20.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    @ResponseBody
    @RESTfulDoc(value = "测试1")
    public void test1() {

    }

    @RequestMapping(value = "/test2", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试2")
    public void test2() {

    }

    @RequestMapping(value = "/test3", method = RequestMethod.GET)
    @ResponseBody
    @RESTfulDoc("测试3")
    public Map<String, Object> test3(@RequestParam String name, @RequestParam int age) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);

        return map;
    }

    @RequestMapping(value = "/test4", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试4")
    public Map<String, Object> test4(@RequestParam String name, @RequestBody Map<String, Object> map) {
        System.err.println("name is:" + name);
        return map;
    }

    @RequestMapping(value = "/test5", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试5")
    public HttpProtocolVO test5(@RequestParam String name, @RequestBody HttpProtocolVO httpProtocolVO) {
        System.err.println("name is:" + name);
        return httpProtocolVO;
    }

    @RequestMapping(value = "/test6", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试6")
    public HttpProtocolVO test6(@RequestParam String name, int sex, int age) {
        System.err.println("name is:" + name + "  sex is:" + sex + "  age is:" + age);
        return new HttpProtocolVO();
    }

    @RequestMapping(value = "/test7", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试7")
    public HttpProtocolVO test7(int sex, int age) {
        System.err.println("sex is:" + sex + "  age is:" + age);
        return new HttpProtocolVO();
    }

    @RequestMapping(value = "/test8/{name}/{age}", method = RequestMethod.GET)
    @ResponseBody
    @RESTfulDoc("测试8")
    public HttpProtocolVO test8(@PathVariable String name, @PathVariable int age) {
        System.err.println("name is:" + name + "  age is:" + age);
        HttpProtocolVO protocolDO = new HttpProtocolVO();
        protocolDO.setPathList(new ArrayList<ProtocolPath>());

        return protocolDO;
    }

    @RequestMapping(value = "/test9", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试9")
    public List test9(int sex, int age) {
        System.err.println("sex is:" + sex + "  age is:" + age);
        return new ArrayList();
    }

    @RequestMapping(value = "/test10", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试10")
    public ArrayList test10(int sex, int age) {
        System.err.println("sex is:" + sex + "  age is:" + age);
        return new ArrayList();
    }

    @RequestMapping(value = "/test11", method = RequestMethod.POST)
    @ResponseBody
    @RESTfulDoc("测试11")
    public ArrayList test11(@Validated @RequestBody HttpProtocolVO protocolDO) {
        return new ArrayList();
    }

    @RequestMapping(value = "/test12/{name}/{age}", method = RequestMethod.GET)
    @ResponseBody
    @RESTfulDoc("测试12")
    public HttpProtocolVO test12(@PathVariable String name, @PathVariable int age, @RequestParam long users) {
        System.err.println("name is:" + name + "  age is:" + age + "   user nums is:" + users);
        HttpProtocolVO protocolDO = new HttpProtocolVO();
        protocolDO.setPathList(new ArrayList<ProtocolPath>());

        return protocolDO;
    }

    @RequestMapping("/test13")
    @RESTfulDoc("测试13")
    public void test13() {

    }

    @RequestMapping("/test14")
    public void test14(String name, @RequestBody Map<String, Object> map) {

    }

    @RequestMapping(value = "/test15", method = RequestMethod.DELETE)
    public void test15(String name, @RequestBody Map<String, Object> map) {

    }

    @RequestMapping(value = "/test16", method = RequestMethod.DELETE)
    public void test16(String name, int age) {
        System.err.println("name is:" + name + "  age is:" + age);
    }

    /**
     * 不指定方法,自识别
     * @param name
     * @param age
     */
    @RequestMapping(value = "/test17")
    public void test17(String name, int age) {
        System.err.println("name is:" + name + "  age is:" + age);
    }

    /**
     * 不指定方法,并响应字符串,并不需要进行精确处理
     * @param name
     * @param age
     * @param size
     * @return
     */
    @RequestMapping("/test18")
    public String test18(String name, int age, @RequestParam int size) {
        return "test18";
    }
}
