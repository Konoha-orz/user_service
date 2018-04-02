package com.pulingle.user_service.service.impl;



import com.pulingle.user_service.domain.entity.User;
import com.pulingle.user_service.domain.entity.User_info;
import com.pulingle.user_service.mapper.UserInfoMapper;
import com.pulingle.user_service.mapper.UserMapper;
import com.pulingle.user_service.service.UserService;
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
    public Map<String, Object> register(String account, String password, String retype_password, String phone, String nickname, String sex, String email, String signature,HttpSession session) {
        Map<String,Object> returnmap = new HashMap<String,Object>();
        String picLocation=String.valueOf(session.getAttribute("picLocation"));
        System.out.println(picLocation);
        boolean phoneFlag=false;
        boolean accountFlag =false;
        boolean nicknameFlag = false;
        boolean emailFlag = false;
        List<User_info> list = userInfoMapper.findAllUserInfo();
        for(int i=0;i<list.size();i++){
            if(phone.equals(list.get(i).getPhone())){
                phoneFlag=true;
            }
            if(account.equals(list.get(i).getAccount())){
                accountFlag=true;
            }
            if(nickname.equals(list.get(i).getNickname())){
                nicknameFlag=true;
            }
            if(email.equals(list.get(i).getEmail())){
                emailFlag=true;
            }
        }
        if(!retype_password.equals(password)){
            returnmap.put("msg","4");
        }else if(account.equals("")){
            returnmap.put("msg","2");
        }else if(nickname.equals("")){
            returnmap.put("msg","3");
        }else if(phoneFlag){
            returnmap.put("msg","5");
        }else if(accountFlag){
            returnmap.put("msg","6");
        }else if(nicknameFlag){
            returnmap.put("msg","7");
        }else if(emailFlag){
            returnmap.put("msg","8");
        }else{
            User_info user_info = new User_info();
            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user_info.setAccount(account);
            user_info.setPhone(phone);
            user_info.setNickname(nickname);
            user_info.setProfile_picture_url(picLocation);
            if(sex.equals("男")){
                user_info.setSex(0);
            }else{
                user_info.setSex(1);
            }
            user_info.setEmail(email);
            user_info.setSignature(signature);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String string_date = sdf.format(date);
            user_info.setCreate_time(string_date);
            try {
                userMapper.insertIntoUser(user);
                int user_id = userMapper.findUserByAccount(user.getAccount()).get(0).getUser_id();
                user_info.setUser_id(user_id);
                userInfoMapper.insertIntoUserInfo(user_info);
                returnmap.put("msg","1");
                session.removeAttribute("picLocation");
            }catch (Exception e){
                returnmap.put("msg","0");
                e.printStackTrace();
            }
        }
        return returnmap;
    }

}
