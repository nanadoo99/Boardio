package com.nki.t1.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class test {
    @GetMapping("/error")
    public void error() throws Exception{
        throw new Exception("hello.all");
    }

    @GetMapping("/ac")
    public void ac(){
        ApplicationContext ac =new GenericXmlApplicationContext("test-config.xml");
        System.out.println("ac.getBeanDefinitionCount() = " + ac.getBeanDefinitionCount());
        System.out.println("ac.getBean(\"testController\") = " + ac.getBean("testController"));
    }
}
