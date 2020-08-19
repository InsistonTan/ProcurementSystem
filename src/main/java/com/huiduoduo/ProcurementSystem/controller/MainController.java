package com.huiduoduo.ProcurementSystem.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/7/8 11:48
 */
@Controller
public class MainController {

    //跳转到首页
    @RequestMapping("/")
    public void index(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        request.getRequestDispatcher("/index.html").forward(request,response);
    }
}
