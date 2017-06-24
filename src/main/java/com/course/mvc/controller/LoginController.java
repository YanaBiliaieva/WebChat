package com.course.mvc.controller;

import com.course.mvc.domain.ChatUser;
import com.course.mvc.domain.RoleEnum;
import com.course.mvc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Admin on 20.05.2017.
 */
@Controller
public class LoginController {
   private final LoginService loginService;
   @Autowired
   public LoginController(LoginService loginService){
       this.loginService=loginService;
    }
    @RequestMapping(value = "/login",method = RequestMethod.GET,name = "loginUser")
    public ModelAndView loginUser(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginHandler","/login");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@RequestParam("login") String login,
                              @RequestParam("password") String password,
                              HttpSession session, HttpServletRequest request,
                              HttpServletResponse response,
                              RedirectAttributes attributes){
        ChatUser chatUser=loginService.verifyLogin(login, password);

       if(Objects.nonNull(chatUser)&&chatUser.getRole().getRole()== RoleEnum.USER){
          session.setAttribute("user", chatUser);
          dropHttpOnlyFlag(session.getId(),request, response);
           return "redirect:/chat";
       }
       if(Objects.nonNull(chatUser)&&chatUser.getRole().getRole()==RoleEnum.ADMIN){
            session.setAttribute("user", chatUser);
            return "redirect:/admin";
        }
        attributes.addFlashAttribute("error","login.incorrect");
        return "redirect:/login";
    }
    //джаваскрипт прочитает только при сброшенном флаге нттр онли
    private void dropHttpOnlyFlag(String sessionId, HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("JSESSIONID")){
                cookie.setHttpOnly(false);
                response.addCookie(cookie);
            }
        }
    }
}
