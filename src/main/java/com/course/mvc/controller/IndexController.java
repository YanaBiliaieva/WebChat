package com.course.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Admin on 20.05.2017.
 */
@Controller
public class IndexController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getHomePage(){
        ModelAndView modelAndView= new ModelAndView();
        modelAndView.setViewName("index");

        return modelAndView;
    }
}
