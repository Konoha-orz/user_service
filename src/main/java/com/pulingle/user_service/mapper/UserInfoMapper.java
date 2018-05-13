package com.pulingle.user_service.mapper;

import com.pulingle.user_service.domain.entity.User_info;
import com.rabbitmq.http.client.domain.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public List<User_info> findUserInfoByUserid(long user_id);

//    /**
//     * @return User_info列表
//     * 查询所有的用户信息对象
//     */
//    @Select("select * from user_info")
//    public List<User_info> findAllUserInfo();

    /**
     * @param user_info 用户信息对象
     * 用于注册是插入用户信息
     */
    @Insert("insert into user_info(user_id,account,nickname,create_time) values(#{user_id},#{account},#{nickname},#{create_time})")
    public void register(User_info user_info);

//    @Update("update user_info set friends_list=#{friends_list} where account=#{account}")
//    public void updateFriendList(User_info user_info);

    /**
    * @param: userInfo
    * @return: int
    * @Des: userInfo表插入一条记录
    */
    int insert(User_info user_info);

    /**
    * @param: account账号
    * @return: List<User_info>
    * @Des: 通过account查找用户信息
    */
    List<User_info> queryUserInfoByAccount(@Param("account")String account);

    /**
    * @param: User_info
    * @return: int
    * @Des: 更新用户个人资料
    */
    int updateUserInfo(User_info user_info);

    /**
    * @param: user_id,profile_picture_url
    * @return: int
    * @Des: 更新用户头像
    */
    int updateProfilePicture(User_info user_info);

    /**
    * @param: name
    * @return: List<Map>
    * @Des: 根据昵称模糊查询用户
    */
    List<Map> searchByNickname(@Param("name")String name,@Param("num")int num);

    /**
    * @param: email
    * @return: int
    * @Des: 查询邮箱数
    */
    int countEmail(@Param("email")String email);
}
