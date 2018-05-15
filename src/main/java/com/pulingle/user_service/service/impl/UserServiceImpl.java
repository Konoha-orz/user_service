package com.pulingle.user_service.service.impl;


import com.alibaba.fastjson.JSON;

import com.pulingle.user_service.domain.dto.MessageDTO;
import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.dto.UserIdListDTO;
import com.pulingle.user_service.domain.entity.*;
import com.pulingle.user_service.feign.OutMessageFeign;
import com.pulingle.user_service.feign.OutMomentFeign;
import com.pulingle.user_service.mapper.OutUserInfoMapper;
import com.pulingle.user_service.mapper.UserInfoMapper;
import com.pulingle.user_service.mapper.UserMapper;
import com.pulingle.user_service.service.UserService;
import com.pulingle.user_service.utils.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Teemo
 * @Description: UserService接口实现
 * @Date: Created in
 */
@Service
public class UserServiceImpl implements UserService {

    private final String FRIENDS_LIST_STR = "FL";
    private final String CAPTCHA_STR = "CAP";
    private final String TOKEN_STR = "TOKEN";
    //TOKEN时长(毫秒)
    private final long TOKEN_TIME = 24 * 60 * 60 * 1000;

    private final long CAPTCHA_TIME = 60;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OutMessageFeign outMessageFeign;

    @Autowired
    private OutMomentFeign outMomentFeign;

    @Autowired
    private OutUserInfoMapper outUserInfoMapper;

    @Override
    public RespondBody login(String account, String password, String captcha, String timeStamp) {
        RespondBody respondBody;
        int momentNum = 0;
        long friendNum = 0;
        try {
            if (account == null || account.equals("") || password == null || password.equals("") || captcha.equals("") || captcha == null)
                return RespondBuilder.buildErrorResponse("参数缺省");
            //登录前先验证验证码
            String redisCap = stringRedisTemplate.opsForValue().get(CAPTCHA_STR + timeStamp);
            if (redisCap == null || redisCap.equals(""))
                return RespondBuilder.buildErrorResponse("验证码失效，请重新获取验证码");
            if (!captcha.equalsIgnoreCase(redisCap))
                return RespondBuilder.buildErrorResponse("验证码错误");
            if (userMapper.existUser(account) < 1)
                return RespondBuilder.buildErrorResponse("账号不存在");
            String password_db = userMapper.findPasswordByAccount(account);
            if (MD5.verify(password, password_db)) {
                //生成Token令牌
                User_info user_info = userInfoMapper.queryUserInfoByAccount(account).get(0);
                String subject = TokenUtil.generalSubject(user_info);
                String token = TokenUtil.createJWT(String.valueOf(user_info.getUser_id()), subject, TOKEN_TIME);
                //Token存入Redis
                stringRedisTemplate.opsForValue().set(TOKEN_STR + user_info.getUser_id(), token, TOKEN_TIME, TimeUnit.MILLISECONDS);
                //通过Feign调用获取用户发布动态数
                UserBasicInfo userBasicInfo = new UserBasicInfo();
                userBasicInfo.setUserId(user_info.getUser_id());
                RespondBody momentRespondBody = outMomentFeign.getMomentsNum(userBasicInfo);
                if (momentRespondBody.getStatus().equals("1"))
                    momentNum = (int) momentRespondBody.getData();
                //获取好友数
                if (stringRedisTemplate.hasKey(FRIENDS_LIST_STR + user_info.getUser_id()))
                    friendNum = stringRedisTemplate.opsForSet().size(FRIENDS_LIST_STR + user_info.getUser_id());
                Map data = new HashMap();
                data.put("userInfo", user_info);
                data.put("token", token);
                data.put("momentNum", momentNum);
                data.put("friendNum", friendNum);
                respondBody = RespondBuilder.buildNormalResponse(data);
            } else
                return RespondBuilder.buildErrorResponse("密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    //2018年4月13日16:26:58
    @Override
    public RespondBody register(String account, String password, String nickname) {
        RespondBody respondBody;
        try {
            if (account == null || password == null || nickname == null || account.equals("") || password.equals("") | nickname.equals(""))
                return RespondBuilder.buildErrorResponse("参数缺省");
            if (userMapper.countAccount(account) > 0)
                return RespondBuilder.buildErrorResponse("账号已存在");
            if (password.length() < 6)
                return RespondBuilder.buildErrorResponse("密码长度小于6位");
            else {
                //密码加密
                String encodedPassword = MD5.generate(password);
                User user = new User();
                user.setAccount(account);
                user.setPassword(encodedPassword);
                userMapper.insert(user);
                //插入user_info表
                User_info user_info = new User_info();
                user_info.setAccount(user.getAccount());
                user_info.setNickname(nickname);
                user_info.setUser_id(user.getUser_id());
                Date date = new Date();
                user_info.setCreate_time(date);
                user_info.setFriends_list(FRIENDS_LIST_STR + user.getUser_id());
                userInfoMapper.insert(user_info);
                respondBody = RespondBuilder.buildNormalResponse("注册成功，用户Id:" + user.getUser_id());
            }
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody acceptFriendRequest(long userId, long friendId, long messageId) {
        RespondBody respondBody;
        try {
            stringRedisTemplate.opsForSet().add("FL" + userId, String.valueOf(friendId));//在调用者的好友列表中添加请求者的id
            stringRedisTemplate.opsForSet().add("FL" + friendId, String.valueOf(userId));//在请求者的好友列表中添加调用者的id
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageId(messageId);
            outMessageFeign.deleteMessage(messageDTO);//根据传入的messageid调用message_service中的删除消息接口删除已处理的好友请求
            //给双方发送新好友消息
            Message messageA = new Message();
            Message messageB = new Message();
            Date date = new Date();
            messageA.setType(1);
            messageB.setType(1);
            messageA.setSendUserId(userId);
            messageB.setSendUserId(friendId);
            messageA.setReceUserId(friendId);
            messageB.setReceUserId(userId);
            messageA.setContent("现在我们是好友了");
            messageB.setContent("现在我们是好友了");
            messageA.setSendTime(date);
            messageB.setSendTime(date);
            outMessageFeign.sendMessage(messageA);
            outMessageFeign.sendMessage(messageB);
            respondBody = RespondBuilder.buildNormalResponse("调用成功");
        } catch (Exception e) {
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
            //通过Feign调用message-service删除与好友之间的消息记录
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setUserId(userId);
            messageDTO.setFriendId(friendId);
            RespondBody messageRespondBody = outMessageFeign.deleteFriendMessage(messageDTO);
            if (messageRespondBody.getStatus().equals("1"))
                respondBody = RespondBuilder.buildNormalResponse("调用成功");
            else
                respondBody = RespondBuilder.buildErrorResponse("消息记录删除错误");
        } catch (Exception e) {
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
            e.printStackTrace();
        }
        return respondBody;
    }


    @Override
    public RespondBody getFriendInfoList(long userId) {
        RespondBody respondBody;
        try {
            User_info ui = userInfoMapper.findUserInfoByUserid(userId).get(0);//获得当前用户的对象
            String friendList = ui.getFriends_list(); //获得当前用户的好友列表key
            Set<String> s = stringRedisTemplate.opsForSet().members(friendList);//通过key获得redis中的set
            List<Map> returnlist = new ArrayList<>();//定义返回列表
            for (String friendId : s) {//遍历set
                Map<String, Object> m = new HashMap<>();
                m.put("userId", friendId);//获得好友id
                m.put("profilePictureUrl", userInfoMapper.findUserInfoByUserid(Long.valueOf(friendId)).get(0).getProfile_picture_url());//获得好友头像地址
                m.put("nickname", userInfoMapper.findUserInfoByUserid(Long.valueOf(friendId)).get(0).getNickname());//获得好友昵称
                returnlist.add(m);
            }
            respondBody = RespondBuilder.buildNormalResponse(returnlist);//返回对象添加
        } catch (Exception e) {
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
            e.printStackTrace();
        }
        return respondBody;
    }

    @Override
    public RespondBody getCaptcha(String timeStamp) {
        RespondBody respondBody;
        try {
            if (timeStamp == null || timeStamp.equals(""))
                return RespondBuilder.buildErrorResponse("account不能为空");
            RandomCodeUtil randomCodeUtil = new RandomCodeUtil();
            //获取4位随机字符串
            String randomStr = randomCodeUtil.getStringRandom(4);
            //字符串存入Redis
            stringRedisTemplate.opsForValue().set(CAPTCHA_STR + timeStamp, randomStr, CAPTCHA_TIME, TimeUnit.SECONDS);
            //随机字符串转出base64验证码
            String randomBase64Code = randomCodeUtil.imageToBase64(120, 40, randomStr);
            respondBody = RespondBuilder.buildNormalResponse(randomBase64Code);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody updateUserInfo(User_info user_info) {
        RespondBody respondBody;
        try {
            User_info newUserInfo = new User_info();
            if (user_info.getUser_id() == 0 || user_info.getNickname() == null || user_info.getNickname().equals(""))
                return RespondBuilder.buildErrorResponse("参数缺省");
            else {
                newUserInfo.setUser_id(user_info.getUser_id());
                newUserInfo.setNickname(user_info.getNickname());
            }
            if (!(user_info.getSex() == 0 || user_info.getSex() == 1))
                return RespondBuilder.buildErrorResponse("性别只能为0或1");
            else
                newUserInfo.setSex(user_info.getSex());
            newUserInfo.setSignature(user_info.getSignature());
            if (!(user_info.getEmail() == null || user_info.getEmail().equals(""))) {
                //邮箱合法校验
                String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
                //正则表达式的模式
                Pattern p = Pattern.compile(RULE_EMAIL);
                //正则表达式的匹配器
                Matcher m = p.matcher(user_info.getEmail());
                if (m.matches()) {
                    //查询现在自己信息
                    User_info nowUser = userInfoMapper.queryUserInfoById(user_info.getUser_id());
                    if (nowUser.getEmail() == null) {
                        if (userInfoMapper.countEmail(user_info.getEmail()) > 0)
                            return RespondBuilder.buildErrorResponse("邮箱已使用");
                        else
                            newUserInfo.setEmail(user_info.getEmail());
                    }else{
                        //不跟自己的相等
                        if(!nowUser.getEmail().equals(user_info.getEmail())) {
                            if (userInfoMapper.countEmail(user_info.getEmail()) > 0)
                                return RespondBuilder.buildErrorResponse("邮箱已使用");
                            else
                                newUserInfo.setEmail(user_info.getEmail());
                        }else
                            newUserInfo.setEmail(nowUser.getEmail());
                    }

                } else
                    return RespondBuilder.buildErrorResponse("邮箱不合法");
            }
            userInfoMapper.updateUserInfo(newUserInfo);
            User_info respondUser = userInfoMapper.queryUserInfoById(user_info.getUser_id());
            respondBody = RespondBuilder.buildNormalResponse(respondUser);
        } catch (Exception e) {
            e.printStackTrace();
            return RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody saveProfilePicture(User_info user_info) {
        RespondBody respondBody;
        try {
            if (user_info.getUser_id() == 0 || user_info.getProfile_picture_url() == null || user_info.getProfile_picture_url().equals(""))
                return RespondBuilder.buildErrorResponse("参数缺省");
            userInfoMapper.updateProfilePicture(user_info);
            respondBody = RespondBuilder.buildNormalResponse("保存头像成功");
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody tokenResolve(String token) {
        RespondBody respondBody;
        int momentNum = 0;
        long friendNum = 0;
        try {
            //先解析该token字符串，若没失效则解析成功
            Claims claims = TokenUtil.parseJWT(token);
            String subject = claims.getSubject();
            User_info user_info = JSON.parseObject(subject, User_info.class);
            //如果该token解析成功，判断redis中的是否失效
            if (stringRedisTemplate.hasKey(TOKEN_STR + user_info.getUser_id())) {
                String redisToken = stringRedisTemplate.opsForValue().get(TOKEN_STR + user_info.getUser_id());
                if (redisToken.equals(token)) {
                    //通过Feign调用获取用户发布动态数
                    UserBasicInfo userBasicInfo = new UserBasicInfo();
                    userBasicInfo.setUserId(user_info.getUser_id());
                    RespondBody momentRespondBody = outMomentFeign.getMomentsNum(userBasicInfo);
                    if (momentRespondBody.getStatus().equals("1"))
                        momentNum = (int) momentRespondBody.getData();
                    //获取好友数
                    if (stringRedisTemplate.hasKey(FRIENDS_LIST_STR + user_info.getUser_id()))
                        friendNum = stringRedisTemplate.opsForSet().size(FRIENDS_LIST_STR + user_info.getUser_id());
                    HashMap data = new HashMap();
                    data.put("userInfo", user_info);
                    data.put("momentNum", momentNum);
                    data.put("friendNum", friendNum);
                    respondBody = RespondBuilder.buildNormalResponse(data);
                } else
                    respondBody = RespondBuilder.buildErrorResponse("Token过期，请重新登录");
            } else {
                respondBody = RespondBuilder.buildErrorResponse("Token过期，请重新登录");
            }
        } catch (Exception e) {
            respondBody = RespondBuilder.buildErrorResponse("Token过期，请重新登录");
        }
        return respondBody;
    }

    @Override
    public RespondBody logout(String token) {
        RespondBody respondBody;
        try {
            Claims claims = TokenUtil.parseJWT(token);
            String subject = claims.getSubject();
            User_info user_info = JSON.parseObject(subject, User_info.class);
            String redisToken = TOKEN_STR + user_info.getUser_id();
            if (stringRedisTemplate.opsForValue().get(redisToken).equals(token)) {
                //清空redis中对应的token
                stringRedisTemplate.delete(redisToken);
                respondBody = RespondBuilder.buildNormalResponse(null);
            } else
                respondBody = RespondBuilder.buildNormalResponse("Token已过期");
        } catch (Exception e) {
            respondBody = RespondBuilder.buildNormalResponse("Token已过期");
        }
        return respondBody;
    }

    @Override
    public RespondBody queryFriendMomentStatus(long userId, int num) {
        RespondBody respondBody;
        try {
            if (userId == 0)
                return RespondBuilder.buildErrorResponse("userId不能为0");
            String friendListKey = FRIENDS_LIST_STR + String.valueOf(userId);
            if (!stringRedisTemplate.hasKey(friendListKey))
                return RespondBuilder.buildNormalResponse(null);
            Set<String> idSet = stringRedisTemplate.opsForSet().members(friendListKey);
            List<String> idList = new ArrayList<>(idSet);
            //通过Feign调用，获取好友近期最新发布动态的时间
            UserIdListDTO userIdListDTO = new UserIdListDTO();
            userIdListDTO.setIdList(idList);
            RespondBody feignRepondBody = outMomentFeign.queryFriendNewMomentTime(userIdListDTO);
            if (feignRepondBody.getStatus().equals("0"))
                respondBody = RespondBuilder.buildErrorResponse("获取好友最新动态状况失败");
            else {

                LinkedHashMap linkedHashMap = (LinkedHashMap) feignRepondBody.getData();
                List<Map> resultList = new ArrayList<>();
                if (linkedHashMap != null) {
                    if (linkedHashMap.size() > 0) {
                        //遍历linkedHashMap
                        Iterator iterator = linkedHashMap.entrySet().iterator();
                        int i = 0;
                        while (iterator.hasNext()) {
                            //显示朋友个数
                            if (i == num)
                                break;
                            Map.Entry entry = (Map.Entry) iterator.next();
                            String id = (String) entry.getKey();
                            Map moment = (Map) entry.getValue();
                            Map userInfo = outUserInfoMapper.getUserInfo(Long.valueOf(id));
                            //获取时间差信息
                            String time = (String) moment.get("time");
                            userInfo.put("time", time);
                            resultList.add(userInfo);
                            i++;
                        }
                    }
                }
                respondBody = RespondBuilder.buildNormalResponse(resultList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody searchByNickname(String name, int num) {
        RespondBody respondBody;
        try {
            if (name.equals("") || name == null) {
                return RespondBuilder.buildNormalResponse(null);
            }
            if (num < 0)
                return RespondBuilder.buildErrorResponse("num不能小于0");
            List<Map> resultList = userInfoMapper.searchByNickname(name, num);
            respondBody = RespondBuilder.buildNormalResponse(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody checkEmail(String email) {
        RespondBody respondBody;
        try {
            if (userInfoMapper.countEmail(email) > 0)
                respondBody = RespondBuilder.buildNormalResponse("0");
            else
                respondBody = RespondBuilder.buildNormalResponse("1");
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }
}
