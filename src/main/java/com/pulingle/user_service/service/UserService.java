package com.pulingle.user_service.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: Teemo
 * @Description: 用户服务
 * @Date: Created in
 */
public interface UserService {

    Map<String,Object> checkUser(String account, String password, HttpSession session);

    Map<String,Object> register(String account, String password, String retype_password, String phone, String nickname, String sex, String email, String signature, HttpSession session);

}
