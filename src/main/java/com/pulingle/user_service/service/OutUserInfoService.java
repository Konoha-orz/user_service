package com.pulingle.user_service.service;

import com.pulingle.user_service.domain.dto.RespondBody;

import java.util.List;

/**
 * Created by @杨健 on 2018/4/7 17:14
 *
 * @Des: 对外提供的用户信息服务
 */

public interface OutUserInfoService {
    RespondBody getUserBasicInfo(List<String> idList);
}
