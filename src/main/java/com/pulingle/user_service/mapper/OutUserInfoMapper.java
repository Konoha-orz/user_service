package com.pulingle.user_service.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by @杨健 on 2018/4/7 16:53
 *
 * @Des: 对外服务的用户信息Mapper
 */
@Component("outUserInfoMapper")
public interface OutUserInfoMapper {
    /**
    * @param: idList(用户Id列表)
    * @return: List<Map>（用户基础信息列表）
    * @Des: 根据用户Id列表查询用户基础信息
    */
    List<Map> getUserBasicInfo(@Param("idList") List<String> idList);
}
