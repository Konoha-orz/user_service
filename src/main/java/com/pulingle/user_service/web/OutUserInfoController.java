package com.pulingle.user_service.web;

import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.dto.UserIdListDTO;
import com.pulingle.user_service.domain.entity.UserBasicInfo;
import com.pulingle.user_service.domain.entity.User_info;
import com.pulingle.user_service.service.OutUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by @杨健 on 2018/4/7 17:19
 *
 * @Des: 对外提供的用户服务Controller
 */
@RestController
@RequestMapping(value = "outUserInfo")
public class OutUserInfoController {

    @Resource
    private OutUserInfoService outUserInfoService;

    @RequestMapping(value = "/getUserBasicInfo",method = RequestMethod.POST)
    public RespondBody getUserBasicInfo(@RequestBody UserIdListDTO userIdListDTO){
        return outUserInfoService.getUserBasicInfo(userIdListDTO.getIdList());
    }

    @RequestMapping(value = "/getFriendBasicInfoList",method = RequestMethod.POST)
    public RespondBody getUserFriendList(@RequestBody UserIdListDTO userIdListDTO){
        return outUserInfoService.getFriendBasicInfoList(userIdListDTO.getUserId());
    }

    @RequestMapping(value = "/getFriendInfoForMoment",method = RequestMethod.POST)
    public RespondBody getFriendInfoForMoment(@RequestBody UserIdListDTO userIdListDTO){
        return outUserInfoService.getFriendInfoForMoment(userIdListDTO.getUserId());
    }

    /**
    * @param: userId
    * @return: RespondBody
    * @Des: 获取用户好友Id列表
    */
    @RequestMapping(value = "/getFriendList",method = RequestMethod.POST)
    public RespondBody getFriendList(@RequestBody UserIdListDTO userIdListDTO){
        return outUserInfoService.getFriendList(userIdListDTO.getUserId());
    }

    /**
     * @param: idList(用户Id列表)
     * @return: RespondBody
     * @Des: 根据用户Id列表查询基本信息，用于动态服务
     */
    @RequestMapping(value = "/getUserBasicInfoForMoment",method = RequestMethod.POST)
    public RespondBody getUserBasicInfoForMoment(@RequestBody UserIdListDTO userIdListDTO){
        return outUserInfoService.getUserBasicInfoForMoment(userIdListDTO.getIdList());
    }

    /**
     * @param: userId用户Id
     * @return: RespondBody
     * @Des: 推荐用户Id列表（好友的好友的集合作并差处理）
     */
    @RequestMapping(value = "/getUnionFriend",method = RequestMethod.POST)
    public RespondBody getUnionFriend(@RequestBody UserIdListDTO userIdListDTO){
        return outUserInfoService.getSCFriendList(userIdListDTO.getUserId());
    }

    /**
    * @param: userId,friendList
    * @return: RespondBody
    * @Des: 判断是否为好友
    */
    @RequestMapping(value = "/isFriend",method = RequestMethod.POST)
    public RespondBody isFriend(@RequestBody User_info user_info){
        return outUserInfoService.isFriend(user_info.getUser_id(),user_info.getFriends_list());
    }

    /**
     * @param: userId
     * @return: Map
     * @Des: 根据用户ID查询用户信息
     */
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public RespondBody getUserInfo(@RequestBody UserBasicInfo userBasicInfo){
        return outUserInfoService.getUserInfo(userBasicInfo.getUserId());
    }

    /**
     * @param调用接口的用户id
     * @return 返回体
     * 通过用户id查询用户的好友列表长度
     */
    @RequestMapping(value = "/getFriendAmount",method = RequestMethod.POST)
    public RespondBody getFriendAmount(@RequestBody UserBasicInfo user){
        return outUserInfoService.getFriendAmount(user.getUserId());
    }

    /**
     * @param: userId
     * @return: RespondBody
     * @Des: 获取好友列表，以及最消息提醒状态
     */
    @RequestMapping(value = "/getFriendWithMessageList",method = RequestMethod.POST)
    public RespondBody getFriendWithMessageList(@RequestBody UserBasicInfo userBasicInfo){
        return outUserInfoService.getFriendWithMessageList(userBasicInfo.getUserId());
    }
}
