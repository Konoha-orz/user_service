package com.pulingle.user_service.service.impl;



import com.pulingle.user_service.domain.entity.User;
import com.pulingle.user_service.domain.entity.User_info;
import com.pulingle.user_service.mapper.UserInfoMapper;
import com.pulingle.user_service.mapper.UserMapper;
import com.pulingle.user_service.service.UserService;
import com.pulingle.user_service.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public Map<String,Object> checkUser(String account, String password, HttpSession session) {
        Map<String,Object> returnmap = new HashMap<String,Object>();
        if(account.equals("")||password.equals("")){
            returnmap.put("msg","0");
        }else {
            String password_db=userMapper.findPasswordByAccount(account);
            if(password_db.equals(password)){
                User u = userMapper.findUserByAccount(account).get(0);
                returnmap.put("msg","1");
                session.setAttribute("user",u);
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
        System.out.println(passwordAfterMd5);
        userMapper.insertIntoUser(user);//先插入user表，以获得userid
        int user_id = userMapper.findUserByAccount(account).get(0).getUser_id();
        user_info.setUser_id(user_id);
        user_info.setAccount(account);
        user_info.setNickname(nickname);
        Date date = new Date();//生成当前时间
        System.out.println(date);
        user_info.setCreate_time(date);
        userInfoMapper.register(user_info);//插入userinfo表
        returnmap.put("msg","1");
        return returnmap;
    }


}
