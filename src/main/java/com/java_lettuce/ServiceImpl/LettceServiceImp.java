package com.java_lettuce.ServiceImpl;


import com.java_lettuce.Util.SendEmailUtil;
import com.java_lettuce.config.redisConfig;
import com.java_lettuce.entity.User;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Logger
@Service
public class LettceServiceImp {
        private org.slf4j.Logger logger = LoggerFactory.getLogger(redisConfig.class);
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 普通缓存放入
     * @param key 键
     * @return true成功 false失败
     */
    public String getString(String key) {
        if(redisTemplate.hasKey(key)) {
            logger.info("Redis中查询");
            return (String) redisTemplate.opsForValue().get(key);
        }else{
            String val="啊巴阿坝。。";
            redisTemplate.opsForValue().set(key, val);
            logger.info("数据库中查询的");
            return val;
        }
    }


    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @param expireTime 超时时间(秒)
     * @return true成功 false失败
     */
    public Boolean set(String key, String value, int expireTime) {
        try {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    ========================================================测试Hash类型
    /**
     * 判断key是否存在，如果存在 在Redis中查询
     * 如果不存在，在MYSQL中查询，并将结果得到，添加到Redis Hash中
     * @param id
     * @return
     */


    public User selectUserById1(String id){
        if(redisTemplate.opsForHash().hasKey("user",id)){
            logger.info("Redis中查询对象");
            return (User) redisTemplate.opsForHash().get("user",id);
        }else{
            User u=new User();
            u.setId(id);
            u.setName("唷？");
            u.setAge(22);
            logger.info("mysql中查询对象");
            redisTemplate.opsForHash().put("user",id,u);
            return u;
        }
    }



//    ===========================
    SendEmailUtil send=new SendEmailUtil();//发送邮件功能

    public String sendAuthCode(String email) throws Exception {

            String fourRandom = LettceServiceImp.getFourRandom();
            redisTemplate.opsForValue().set(email,fourRandom,40,TimeUnit.SECONDS);
            send.sendEmail(fourRandom,email);

            return fourRandom;
        }

public String getAuthCode(String email){
    System.out.println(redisTemplate.opsForValue().get(email));
       return (String) redisTemplate.opsForValue().get(email);
}




    /**
     * 产生4位随机数(0000-9999)
     * @return 4位随机数
     */
    public static String getFourRandom(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) +"";
        int randLength = fourRandom.length();
        if(randLength<4){
            for(int i=1; i<=4-randLength; i++)
                fourRandom = "0"+ fourRandom ;
        }
        return fourRandom;
    }




    public String judge(String u){
        String keyLoginFail="user:"+u+": login:fail: count";//用户登录失败次数
        String keyLoginStop="user:"+u+": login:stop:time";//用户禁止登录时间

        int num=5;//总登录次数

        //判断用户是否处于限制时间内
        if (redisTemplate.hasKey(keyLoginStop)){

            long stopTime=redisTemplate.getExpire(keyLoginStop,TimeUnit.MINUTES);//剩余 禁止登陆时间
            return  "处于限制登陆状态，请在"+stopTime+"分钟后重新登陆";
        }else {
            if (!redisTemplate.hasKey(keyLoginFail)){//是首次失败
                //设置过期时间要和存值分开，不然会失效。
                redisTemplate.opsForValue().set(keyLoginFail,"1");
                redisTemplate.expire(keyLoginFail,2,TimeUnit.MINUTES);
                return "密码输入错误，2分钟内还剩"+(num-1)+"次机会登陆";

            }else {
                //2分钟内非首次登陆失败
                int failCount=Integer.parseInt(redisTemplate.opsForValue().get(keyLoginFail));
                if (failCount>=num-1){
                    //超过限制次数，冻结帐号
                    redisTemplate.opsForValue().set(keyLoginStop,"1");
                    redisTemplate.expire(keyLoginStop,1,TimeUnit.HOURS);
                    return  "密码输入错误超过五次，冻结帐号一小时";
                }else {
                    redisTemplate.opsForValue().increment(keyLoginFail,1);
                    Long waitTime = redisTemplate.getExpire(keyLoginFail, TimeUnit.SECONDS);

                    return "密码输入错误"+(failCount+1)+"次，在2分钟内还可输入"+(num-(failCount+1))+"次"
                            +"次数将于"+waitTime+"秒后重置";

                }


            }




        }




    }









}
