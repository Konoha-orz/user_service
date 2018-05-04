package com.pulingle.user_service.feign;

import com.pulingle.user_service.domain.dto.MessageDTO;
import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.entity.UserBasicInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Teemo
 * @Description:
 * @Date: Created in 10:19 2018/4/11
 */

@Component("outMessageFeign")
@FeignClient(name="MESSAGE-SERVICE")
@RequestMapping(value = "/message")
public interface OutMessageFeign {

    /**
     * @param messageId 消息id
     * @return 返回体
     * 调用message-service中的接口：通过消息id删除好友请求消息
     */
    @RequestMapping(value="/deleteFriendRequest",method = RequestMethod.POST)
    public  RespondBody deleteMessage(@RequestBody MessageDTO message);

    /**
     * @param: userId
     * @return: RespondBody
     * @Des: 获取用户好友发送最新消息的内容以及userId
     */
    @RequestMapping(value = "/getNewMessageFriendIdList",method = RequestMethod.POST)
    public RespondBody getNewMessageFriendIdList(@RequestBody UserBasicInfo userBasicInfo);

}
