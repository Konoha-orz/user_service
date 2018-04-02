package com.pulingle.user_service.mapper;

import com.pulingle.user_service.domain.entity.User_info;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Teemo
 * @Description:用户信息表mapper
 * @Date: Created in 10:23 2018/3/25
 */

@Mapper
@Component("userInfoMapper")
public interface UserInfoMapper {

    /**
     * @param user_info
     * 将用户信息对象插入用户信息表
     */
    @Insert("insert into user_info values(#{user_id},#{account},#{phone},#{nickname},#{profile_picture_url},#{sex},#{email},#{friends_list},#{signature},#{pictures_list},#{create_time})")
    public void insertIntoUserInfo(User_info user_info);

    /**
     * @param user_id
     * @return User_info列表
     * 通过用户id查询对应的用户信息对象
     */
    @Select("select * from user_info where user_id = #{user_id}")
    public List<User_info> findUserInfoByUserid(int user_id);

    @Select("select * from user_info")
    public List<User_info> findAllUserInfo();
}
