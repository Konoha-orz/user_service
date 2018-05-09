package com.pulingle.user_service.service;

import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.entity.User_info;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: Teemo
 * @Description: 用户服务
 * @Date: Created in
 */
public interface UserService {

    RespondBody login(String account, String password,String captcha,String timeStamp);

    RespondBody register(String account, String password, String nickname);


    RespondBody acceptFriendRequest(long userId, long friendId, long messageId);

    RespondBody deleteFriend(long userId,long friendId);

    RespondBody getFriendInfoList(long userId);

    /**
    * @param: account用户账号
    * @return: RespondBody
    * @Des: 用户登录获取图形验证码服务
    */
    RespondBody getCaptcha(String account);

    /**
    * @param:  user_info
    * @return: RespondBody
    * @Des: 用户个人资料修改
    */
    RespondBody updateUserInfo(User_info user_info);

    /**
    * @param: User_info(user_id,profile_picture_url)
    * @return: RespondBody
    * @Des: 修改用户头像
    */
    RespondBody saveProfilePicture(User_info user_info);

    /**
    * @param: token
    * @return: RespondBody
    * @Des: 根据token获取用户基本信息
    */
    RespondBody tokenResolve(String token);

    /**
    * @param: token
    * @return: RespondBody
    * @Des: 用户登出
    */
    RespondBody logout(String token);

    /**
     * @param: userId,num
     * @return: RespondBody
     * @Des: 获取用户num个好友近1天新发布动态的时间
     */
    RespondBody queryFriendMomentStatus(long userId,int num);

}
