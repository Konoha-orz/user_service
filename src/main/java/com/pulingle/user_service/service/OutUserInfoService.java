package com.pulingle.user_service.service;

import com.pulingle.user_service.domain.dto.RespondBody;

import java.util.List;

/**
 * Created by @杨健 on 2018/4/7 17:14
 *
 * @Des: 对外提供的用户信息服务
 */

public interface OutUserInfoService {
    /**
    * @param:  idList id列表
    * @return: RespondBody
    * @Des: 根据用户id列表获取用户基本信息服务
    */
    RespondBody getUserBasicInfo(List<String> idList);

    /**
    * @param: userId(用户Id)
    * @return: RespondBody
    * @Des: 获取用户好友信息列表
    */
    RespondBody getFriendBasicInfoList(long userId);

    /**
     * @param: userId(用户Id)
     * @return: RespondBody
     * @Des: 获取用户好友信息列表和Id，用于动态服务
     */
    RespondBody getFriendInfoForMoment(long userId);

    /**
    * @param: userId（用户Id）
    * @return:  RespondBody
    * @Des: 获取用户好友Id列表
    */
    RespondBody getFriendList(long userId);

    /**
    * @param: idList(用户Id列表)
    * @return: RespondBody
    * @Des: 根据用户Id列表查询基本信息，用于动态服务
    */
    RespondBody getUserBasicInfoForMoment(List<String> idList);

    /**
    * @param: userId(用户ID)
    * @return: RespondBody（List<String> idList）二度好友Id列表
    * @Des: 根据用户Id,获取二度好友(所有的好友的好友的并集,然后与好友集合做差集)Id列表
    */
    RespondBody getSCFriendList(long userId);

    /**
    * @param: userId,friendsList
    * @return: RespondBody
    * @Des: 根据用户ID，对方好友列表Redis键值,判断是否他好友还是二度好友
    */
    RespondBody isFriend(long userId,String friendList);

    /**
     * @param: userId
     * @return: Map
     * @Des: 根据用户ID查询用户信息
     */
    RespondBody getUserInfo(long userId);

    /**
     * @param调用接口的用户id
     * @return 返回体
     * 通过用户id查询用户的好友列表长度
     */
    RespondBody getFriendAmount(long userId);

    /**
    * @param: userId
    * @return: RespondBody
    * @Des: 获取好友列表，以及最消息提醒状态
    */
    RespondBody getFriendWithMessageList(long userId);

}
