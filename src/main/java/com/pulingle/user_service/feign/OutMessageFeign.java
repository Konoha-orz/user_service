package com.pulingle.user_service.feign;

import com.pulingle.user_service.domain.dto.MessageDTO;
import com.pulingle.user_service.domain.dto.RespondBody;
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
@FeignClient(name="message-service")
public interface OutMessageFeign {

    /**
     * @param messageId 消息id
     * @return 返回体
     * 调用message-service中的接口：通过消息id删除好友请求消息
     */
    @RequestMapping(value="/message/deleteFriendRequest",method = RequestMethod.POST)
    public  RespondBody deleteMessage(@RequestBody MessageDTO message);

}
