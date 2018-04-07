package com.pulingle.user_service.service.impl;

import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.mapper.OutUserInfoMapper;
import com.pulingle.user_service.service.OutUserInfoService;
import com.pulingle.user_service.utils.RespondBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by @杨健 on 2018/4/7 17:22
 *
 * @Des: 对外提供的用户信息服务实现类
 */
@Service("outUserInfoService")
public class OutUserInfoServiceImpl implements OutUserInfoService{

    @Resource
    private OutUserInfoMapper outUserInfoMapper;

    @Override
    public RespondBody getUserBasicInfo(List<String> idList) {
        RespondBody respondBody;
        try{
            List<Map> resultList=outUserInfoMapper.getUserBasicInfo(idList);
            respondBody= RespondBuilder.buildNormalResponse(resultList);
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }
}
