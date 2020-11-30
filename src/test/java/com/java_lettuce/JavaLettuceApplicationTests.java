package com.java_lettuce;

import com.java_lettuce.ServiceImpl.LettceServiceImp;
import com.java_lettuce.ServiceImpl.dataTypeServiceImpl;
import com.java_lettuce.Util.SendEmailUtil;
import com.java_lettuce.entity.User;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class JavaLettuceApplicationTests {

    @Autowired
    LettceServiceImp serviceImp;

    @Autowired
    dataTypeServiceImpl typeServiceImple;
    @Test
    void contextLoads() {
        String nname = serviceImp.getString("NNAME");
        System.out.println(nname);
    }

    @Test
    void  t1(){
        Boolean aBoolean = serviceImp.set("sex", "男", 20);
        System.out.println(aBoolean);

    }

    @Test
    void  t2(){
        User user = serviceImp.selectUserById1("1");
        System.out.println(user);

    }

    @Test
    void t3() throws Exception {
        SendEmailUtil send = new SendEmailUtil();
        send.sendEmail("1146","2324730940@qq.com");

    }
    
    
//    ---
    @Test
    void t4(){
        List<Object> objects=new ArrayList<>();
        objects.add("this time");
        typeServiceImple.lpushAll("fOR",objects);
        
    }
}
