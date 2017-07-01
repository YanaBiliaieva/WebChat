package com.course.mvc.aspect;

import com.course.mvc.dto.ChatUserDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Admin on 27.05.2017.
 */
@ControllerAdvice
public class AspectExceptionHandler {

    /**
     * Не используем редирект
     *
     * @param exception
     * @return
     */
//    @ExceptionHandler(ServiceException.class)
//    public ModelAndView handleServiceException(Exception exception) {
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("error", exception.getMessage());
//        mav.addObject("user", new ChatUserDto());
//        mav.setViewName("registration");
//        return mav;
//    }

    /**
     * For each Spring controller we can simply define a method that automatically gets called if a given exception occurs.
     * Thus whenever an ServiceException is raised from any controller method will call the above method handleServiceException().
     * We mapped ServiceException.class to this method using @ExceptionHandler annotation.
     *
     * Используем редирект
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public RedirectView handleServiceException(ServiceException exception, HttpServletRequest request) {
        RedirectView rw = new RedirectView("./registration");
        rw.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null){
            outputFlashMap.put("error", exception.getMessage());
        }
        return rw;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex){
        ex.printStackTrace();
        return "/index";
    }

    //TODO Change handleServiceException via HttpSession

}
