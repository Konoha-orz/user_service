package com.pulingle.user_service.web;

import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.dto.UserIdListDTO;
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

}
