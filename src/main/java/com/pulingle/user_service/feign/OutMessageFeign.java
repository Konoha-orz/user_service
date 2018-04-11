package com.pulingle.user_service.feign;

import com.pulingle.user_service.domain.dto.RespondBody;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
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
public interface OutMessageFeign {

    /**
     * @param messageId 消息id
     * @return 返回体
     * 调用message-service中的接口：通过消息id删除好友请求消息
     */
    @RequestMapping(value="/Message/deleteFriendRequest",method = RequestMethod.POST)
    public @ResponseBody RespondBody deleteMessage(long messageId);

}
