package com.pulingle.user_service.service.impl;



import com.netflix.discovery.converters.Auto;
import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.entity.User;
import com.pulingle.user_service.domain.entity.User_info;
import com.pulingle.user_service.feign.OutMessageFeign;
import com.pulingle.user_service.mapper.UserInfoMapper;
import com.pulingle.user_service.mapper.UserMapper;
import com.pulingle.user_service.service.UserService;
import com.pulingle.user_service.utils.JwtUtil;
import com.pulingle.user_service.utils.MD5;
import com.pulingle.user_service.utils.RespondBuilder;
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

    @Autowired
    private OutMessageFeign outMessageFeign;

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
        List<User> l = userMapper.findAllUser();
        boolean flag= true;
        for(int k=0;k<l.size();k++){
            if(l.get(k).getAccount().equals(account)){
                flag = false;
            }
        }
        if(flag) {
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
            user_info.setFriends_list("null");
            userInfoMapper.register(user_info);//插入userinfo表
            returnmap.put("msg", "1");//注册成功
        }else{
            returnmap.put("msg","0");//注册失败，用户已存在
        }
        return returnmap;
    }

//    @Override
//    public Map<String, Object> addFriend(String friendAccount) {
//        return null;
//    }

//    @Override
//    public Map<String, Object> addFriend(String friendAccount) {
//        Map<String,Object> returnmap = new HashMap<String,Object>();
//        String token = stringRedisTemplate.opsForValue().get("token");
//        int user_id = Integer.valueOf(JwtUtil.verifyToken(token).getId());
//        User_info user_info = userInfoMapper.findUserInfoByUserid(user_id).get(0);
//        try {
//            if(!friendAccount.equals(user_info.getAccount())) {
//                List<User> list = userMapper.findUserByAccount(friendAccount);
////                System.out.println(list.size());
//                if (list.size() != 0) {
//                    if (user_info.getFriends_list().equals("null")) {
//                        user_info.setFriends_list(friendAccount);
//                        userInfoMapper.updateFriendList(user_info);
//                        returnmap.put("msg", "2");//添加好友成功
//                    } else {
//                        boolean flag = true;
//                        String friends = user_info.getFriends_list();
//                        String[] friend = friends.split(",");
//                        for (int i = 0; i < friend.length; i++) {
//                            if (friendAccount.equals(friend[i])) {
//                                flag = false;
//                            }
//                        }
//                        if (flag) {
//                            StringBuilder sb = new StringBuilder(friends);
//                            sb.append("," + friendAccount);
//                            String friendList = sb.toString();
//                            user_info.setFriends_list(friendList);
//                            userInfoMapper.updateFriendList(user_info);
//                            returnmap.put("msg", "2");//添加好友成功
//                        } else {
//                            returnmap.put("msg", "1");//好友已存在了
//                        }
//                    }
//                } else {
//                    returnmap.put("msg", "0");//这个account不存在
//                }
//            }else {
//                returnmap.put("msg","3");//不能添加自己为好友
//            }
//        }catch (NullPointerException e){
//            e.printStackTrace();
//            returnmap.put("msg","0");//这个account不存在
//        }
//        return returnmap;
//    }

    @Override
    public RespondBody acceptFriendRequest(long userId, long friendId, long messageId) {
        RespondBody respondBody;
        try {
            stringRedisTemplate.opsForSet().add("FL" + userId, String.valueOf(friendId));//在调用者的好友列表中添加请求者的id
            stringRedisTemplate.opsForSet().add("FL" + friendId, String.valueOf(userId));//在请求者的好友列表中添加调用者的id
            outMessageFeign.deleteMessage(messageId);//根据传入的messageid调用message_service中的删除消息接口删除已处理的好友请求
            respondBody = RespondBuilder.buildNormalResponse("调用成功");
        }catch (Exception e){
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
            e.printStackTrace();
        }
        return respondBody;
    }

    @Override
    public RespondBody deleteFriend(long userId, long friendId) {
        RespondBody respondBody;
        try {
            stringRedisTemplate.opsForSet().remove("FL" + userId, String.valueOf(friendId));//在调用者的好友列表中删除请求者的id
            stringRedisTemplate.opsForSet().remove("FL" + friendId, String.valueOf(userId));//在请求者的好友列表中删除调用者的id
            respondBody = RespondBuilder.buildNormalResponse("调用成功");
        }catch (Exception e){
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
            e.printStackTrace();
        }
        return respondBody;
    }

    @Override
    public RespondBody getFriendAmount(long userId) {
        RespondBody respondBody;
        try {
            stringRedisTemplate.opsForSet().size("FL"+userId);//获取好友列表长度
            respondBody = RespondBuilder.buildNormalResponse("调用成功");
        }catch (Exception e){
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
            e.printStackTrace();
        }
        return respondBody;
    }


}
