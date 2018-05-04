package com.pulingle.user_service.service.impl;

import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.entity.UserBasicInfo;
import com.pulingle.user_service.feign.OutMessageFeign;
import com.pulingle.user_service.mapper.OutUserInfoMapper;
import com.pulingle.user_service.service.OutUserInfoService;
import com.pulingle.user_service.utils.RespondBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by @杨健 on 2018/4/7 17:22
 *
 * @Des: 对外提供的用户信息服务实现类
 */
@Service("outUserInfoService")
public class OutUserInfoServiceImpl implements OutUserInfoService {

    final String FRIENDS_LIST_STR = "FL";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OutUserInfoMapper outUserInfoMapper;

    @Autowired
    private OutMessageFeign outMessageFeign;

    @Override
    public RespondBody getUserBasicInfo(List<String> idList) {
        RespondBody respondBody;
        try {
            List<UserBasicInfo> resultList = outUserInfoMapper.getUserBasicInfo(idList);
            respondBody = RespondBuilder.buildNormalResponse(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody getFriendBasicInfoList(long userId) {
        String friendList = FRIENDS_LIST_STR + userId;
        RespondBody respondBody;
        try {
            if (!stringRedisTemplate.hasKey(friendList))
                return RespondBuilder.buildErrorResponse("Redis中不存在该键值");
            //从Redis获取好友Id
            Set<String> idSet = stringRedisTemplate.opsForSet().members(friendList);
            List<String> idList = new ArrayList<>(idSet);
            //获取好友基础信息
            List<UserBasicInfo> resultList = outUserInfoMapper.getUserBasicInfo(idList);
            respondBody = RespondBuilder.buildNormalResponse(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }


    @Override
    public RespondBody getFriendInfoForMoment(long userId) {
        String friendList = FRIENDS_LIST_STR + userId;
        RespondBody respondBody;
        try {
            if (!stringRedisTemplate.hasKey(friendList))
                return RespondBuilder.buildErrorResponse("Redis中不存在该键值");
            //从Redis获取好友Id
            Set<String> idSet = stringRedisTemplate.opsForSet().members(friendList);
            List<String> idList = new ArrayList<>(idSet);
            //获取好友基础信息
            List<UserBasicInfo> resultList = outUserInfoMapper.getUserBasicInfo(idList);
            Map friendMap = new HashMap();
            for (UserBasicInfo userBasicInfo : resultList) {
                friendMap.put("UBI" + userBasicInfo.getUserId(), userBasicInfo);
            }
            friendMap.put("idList", idList);
            respondBody = RespondBuilder.buildNormalResponse(friendMap);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody getFriendList(long userId) {
        String friendList = FRIENDS_LIST_STR + userId;
        RespondBody respondBody;
        try {
            if (!stringRedisTemplate.hasKey(friendList))
                return RespondBuilder.buildErrorResponse("Redis中不存在该键值");
            //从Redis获取好友Id
            Set<String> idSet = stringRedisTemplate.opsForSet().members(friendList);
            List<String> idList = new ArrayList<>(idSet);
            respondBody = RespondBuilder.buildNormalResponse(idList);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody getUserBasicInfoForMoment(List<String> idList) {
        RespondBody respondBody;
        try {
            if (idList == null)
                return RespondBuilder.buildErrorResponse("idList不能为空");
            //获取好友基础信息
            List<UserBasicInfo> resultList = outUserInfoMapper.getUserBasicInfo(idList);
            Map friendMap = new HashMap();
            for (UserBasicInfo userBasicInfo : resultList) {
                friendMap.put("UBI" + userBasicInfo.getUserId(), userBasicInfo);
            }
            respondBody = RespondBuilder.buildNormalResponse(friendMap);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody getSCFriendList(long userId) {
        RespondBody respondBody;
        String friendListKey = FRIENDS_LIST_STR + userId;
        String UNION_FRIEND_KEY = "UFK" + userId;
        String RESULT_SET_KEY="RSK"+userId;
        //最后得到的集合中要返回的元素个数
        long MEMBERS_COUNT=10;
        try {
            //获取用户好友ID集合
            if (!stringRedisTemplate.hasKey(friendListKey))
                return RespondBuilder.buildErrorResponse("Redis中不存在该键值");
            Set<String> friendIdSet = stringRedisTemplate.opsForSet().members(friendListKey);
            //遍历ID集合，求所有好友的好友的并集
            List<String> keyList = new ArrayList<String>();
            for (String key : friendIdSet) {
                keyList.add(FRIENDS_LIST_STR + key);
            }
            //求并集，存储到UNION_FRIEND_KEY集合
            stringRedisTemplate.opsForSet().unionAndStore(friendListKey, keyList, UNION_FRIEND_KEY);
            //新集合与好友集合做差存储到RESULT_SET_KEY的Set里
            stringRedisTemplate.opsForSet().differenceAndStore(UNION_FRIEND_KEY,friendListKey,RESULT_SET_KEY);
            //排除自己
            stringRedisTemplate.opsForSet().remove(RESULT_SET_KEY,String.valueOf(userId));
            //随机返回集合中的N个元素
            Set<String> resultSet=stringRedisTemplate.opsForSet().distinctRandomMembers(RESULT_SET_KEY,MEMBERS_COUNT);
            //清空上述的集合
            stringRedisTemplate.delete(UNION_FRIEND_KEY);
            stringRedisTemplate.delete(RESULT_SET_KEY);
            //获得该集合用户的基础信息
            List<String> idList=new ArrayList<>(resultSet);
            List<UserBasicInfo> resultList = outUserInfoMapper.getUserBasicInfo(idList);
            Map friendMap = new HashMap();
            for (UserBasicInfo userBasicInfo : resultList) {
                friendMap.put("UBI" + userBasicInfo.getUserId(), userBasicInfo);
            }
            friendMap.put("idList",idList);
            respondBody=RespondBuilder.buildNormalResponse(friendMap);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody isFriend(long userId, String friendList) {
        RespondBody respondBody;
        try{
            if(stringRedisTemplate.hasKey(friendList)) {
                //判断是否为好友
                if (stringRedisTemplate.opsForSet().isMember(friendList, String.valueOf(userId)))
                    respondBody = RespondBuilder.buildNormalResponse(1);
                else
                    respondBody = RespondBuilder.buildNormalResponse(0);
            }else
                respondBody = RespondBuilder.buildNormalResponse(0);
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }



    @Override
    public RespondBody getUserInfo(long userId) {
        RespondBody respondBody;
        try {
            if(userId==0)
                return RespondBuilder.buildErrorResponse("UserId不能为0");
            else {
                Map userInfo=outUserInfoMapper.getUserInfo(userId);
                respondBody=RespondBuilder.buildNormalResponse(userInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody getFriendAmount(long userId) {
        RespondBody respondBody;
        try {
            String key=FRIENDS_LIST_STR + userId;
            long amount = stringRedisTemplate.opsForSet().size(key);//获取好友列表长度
            respondBody = RespondBuilder.buildNormalResponse(String.valueOf(amount));//返回值添加
        } catch (Exception e) {
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
            e.printStackTrace();
        }
        return respondBody;
    }

    @Override
    public RespondBody getFriendWithMessageList(long userId) {
        RespondBody respondBody;
        List<Map> resultList=new ArrayList<>();
        ArrayList<Integer> idSet;
//        List<Integer> idSet;
        HashMap contentMap=new HashMap();
        try{
            //通过Feign调用获取最新好友消息及其ID列表
            UserBasicInfo userBasicInfo=new UserBasicInfo();
            userBasicInfo.setUserId(userId);
            RespondBody friendRespondBody=outMessageFeign.getNewMessageFriendIdList(userBasicInfo);
            //获取Redis中好友集合
            Set<String> friendSet=stringRedisTemplate.opsForSet().members(FRIENDS_LIST_STR+String.valueOf(userId));
            if (friendRespondBody.getStatus().equals("1")){
                Map respondMap= (Map) friendRespondBody.getData();
                idSet= (ArrayList<Integer>) respondMap.get("ideSet");
                contentMap= (HashMap) respondMap.get("contentMap");
                for(int str:idSet){
                    Map userInfoMap=outUserInfoMapper.getUserInfo(Long.valueOf(str));
                    userInfoMap.put("content",contentMap.get(String.valueOf(str)));
                    resultList.add(userInfoMap);
                }
                //去除有新消息的ID
                friendSet.removeAll(idSet);
                List<String> friendList=new ArrayList<>(friendSet);
                List newUserInfoList=outUserInfoMapper.getUserBasicInfo(friendList);
                resultList.addAll(newUserInfoList);
                respondBody=RespondBuilder.buildNormalResponse(resultList);
            }else {
                List<String> idList=new ArrayList<>(friendSet);
                List<UserBasicInfo> result=outUserInfoMapper.getUserBasicInfo(idList);
                respondBody=RespondBuilder.buildNormalResponse(result);
            }
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }
}
