package com.pulingle.user_service.service.impl;



import com.netflix.discovery.converters.Auto;
import com.pulingle.user_service.domain.entity.User;
import com.pulingle.user_service.domain.entity.User_info;
import com.pulingle.user_service.mapper.UserInfoMapper;
import com.pulingle.user_service.mapper.UserMapper;
import com.pulingle.user_service.service.UserService;
import com.pulingle.user_service.utils.JwtUtil;
import com.pulingle.user_service.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Teemo
 * @Description: UserService接口实现
 * @Date: Created in
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Map<String,Object> login(String account, String password) {
        Map<String,Object> returnmap = new HashMap<String,Object>();
        if(account.equals("")||password.equals("")){
            returnmap.put("msg","0");
        }else {
            String password_db=userMapper.findPasswordByAccount(account);
            MD5 md5 = new MD5();
            System.out.println(md5.verify(password,password_db));
            if(md5.verify(password,password_db)){
                User u = userMapper.findUserByAccount(account).get(0);
                String token = JwtUtil.generToken(String.valueOf(u.getUser_id()),null,null);
                stringRedisTemplate.opsForValue().set("token",token,300, TimeUnit.SECONDS);
                returnmap.put("token",token);
                returnmap.put("msg","1");
            }else{
                returnmap.put("msg","2");
            }
        }
        return returnmap;
    }

    @Override
    public Map<String, Object> register(String account, String password, String nickname) {
        Map<String,Object> returnmap = new HashMap<String,Object>();
        MD5 md5 = new MD5();
        User user = new User();
        User_info user_info = new User_info();
        String passwordAfterMd5 = md5.generate(password);//加密密码
        user.setAccount(account);
        user.setPassword(passwordAfterMd5);
        userMapper.insertIntoUser(user);//先插入user表，以获得userid
        int user_id = userMapper.findUserByAccount(account).get(0).getUser_id();
        user_info.setUser_id(user_id);
        user_info.setAccount(account);
        user_info.setNickname(nickname);
        Date date = new Date();//生成当前时间
        user_info.setCreate_time(date);
        userInfoMapper.register(user_info);//插入userinfo表
        returnmap.put("msg","1");
        return returnmap;
    }

    @Override
    public Map<String, Object> addFriend(String friendAccount) {
        Map<String,Object> returnmap = new HashMap<String,Object>();

        return returnmap;
    }


}
