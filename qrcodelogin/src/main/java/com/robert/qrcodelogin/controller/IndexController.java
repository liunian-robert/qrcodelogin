package com.robert.qrcodelogin.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/9/30 15:07
 * @描述:
 */
@Controller
public class IndexController {

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String go2Index(){
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "redirect:/index";
    }
}
