package com.pulingle.user_service.service;

import com.pulingle.user_service.domain.dto.RespondBody;
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

    Map<String,Object> login(String account, String password);

    Map<String,Object> register(String account, String password, String nickname);

//    Map<String,Object> addFriend(String friendAccount);

    RespondBody acceptFriendRequest(long userId, long friendId, long messageId);

    RespondBody deleteFriend(long userId,long friendId);

    RespondBody getFriendAmount(long userId);

    RespondBody getFriendInfoList(long userId);
}
