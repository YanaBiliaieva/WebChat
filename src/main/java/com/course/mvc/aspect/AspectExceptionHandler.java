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
@ControllerAdvice //типичный аспект
public class AspectExceptionHandler {
    //все, что касается побочной ветки
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
    //TODO change server exception via httpsesssion
}
