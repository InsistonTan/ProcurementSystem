package com.huiduoduo.procurement_system.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
        request.getRequestDispatcher("/demo.html").forward(request,response);
    }
}
