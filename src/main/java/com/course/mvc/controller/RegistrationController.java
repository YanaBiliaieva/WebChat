package com.course.mvc.controller;

import com.course.mvc.dto.ChatUserDto;
import com.course.mvc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Admin on 20.05.2017.
 */
@Controller
public class RegistrationController {

    private LoginService loginService;
    @Autowired
    public RegistrationController(LoginService loginService){
        this.loginService=loginService;
    }

    @RequestMapping(value = "/registration",method = RequestMethod.GET, name = "registrationUser")
    public String registrationUser(Model model){
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new ChatUserDto());
        }
        return "registration";
    }

    @RequestMapping(value = "/registration",method = RequestMethod.POST)
    public String saveUser(@Validated @ModelAttribute("user") ChatUserDto chatUserDto,
                           BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.user",result);
            chatUserDto.setPassword("");
            attributes.addFlashAttribute("user",chatUserDto);
            return "redirect:/registration";
        }
        System.out.println("IN_SAVE_REGISTRATION");
        loginService.save(chatUserDto);
        return "redirect:/";
    }

}
