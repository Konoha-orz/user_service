package com.pulingle.user_service.feign;

import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.dto.UserIdListDTO;
import com.pulingle.user_service.domain.entity.UserBasicInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by @杨健 on 2018/5/5 14:59
 *
 * @Des: Out动态服务Feign调用
 */
@Component("outMomentFeign")
@FeignClient(name="MOMENT-SERVICE")
@RequestMapping(value = "/outMoment")
public interface OutMomentFeign {

    /**
     * @param: userId
     * @return: RespondBody
     * @Des: 获取用户发布动态数
     */
    @RequestMapping(value = "/getMomentsNum",method = RequestMethod.POST)
    public RespondBody getMomentsNum(@RequestBody UserBasicInfo userBasicInfo);

    /**
     * @param: userId,num
     * @return: RespondBody
     * @Des: 获取用户num个好友近1天新发布动态的时间及好友ID
     */
    @RequestMapping(value = "/queryFriendNewMomentTime",method = RequestMethod.POST)
    public RespondBody queryFriendNewMomentTime(UserIdListDTO userIdListDTO);
}
