package com.java_lettuce.Controller;

import com.java_lettuce.ServiceImpl.LettceServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class controller {
    @Autowired
    LettceServiceImp serviceImp;


    @ResponseBody
    @RequestMapping("/send")
    public  String  send(HttpServletRequest request) throws Exception {
        String email = request.getParameter("email");

        String authCodeInRedis = serviceImp.sendAuthCode(email);
    return "验证码发送成功";
    }


    @ResponseBody
    @RequestMapping("/check")
    public boolean check(HttpServletRequest request){
        String email = request.getParameter("email");
        String authCode = request.getParameter("authCode");
        if (authCode.equals(serviceImp.getAuthCode(email))){

            return  true;
        }else {
            return  false;
        }

    }



    @ResponseBody
    @RequestMapping("/judge")
    public  String  judge(HttpServletRequest request){

        String u=request.getParameter("user");
        String password=request.getParameter("password");

        if (password.equals("123")){

            return "登录成功";
        }else {
            String info = serviceImp.judge(u);

            return info;
        }


    }


    @RequestMapping("/index2")

    public String index2(){

        return "index2";
    }

}
