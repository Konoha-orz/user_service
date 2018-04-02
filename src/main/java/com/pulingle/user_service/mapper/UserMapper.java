package com.pulingle.user_service.mapper;

import com.pulingle.user_service.domain.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Teemo
 * @Description:用户表Mapper
 * @Date: Created in 10:03 2018/3/25
 */

@Mapper
@Component("userMapper")
public interface UserMapper {

    /**
     * @param user
     * 将用户对象插入用户表
     */
    @Insert("insert into user(account,password) values(#{account},#{password})")
    public void insertIntoUser(User user);

    /**
     * @param user_id
     * @return User列表
     * 通过用户id查询对应的用户对象
     */
    @Select("select * from user where user_id = #{userid}")
    public List<User> findUserById(int user_id);

    /**
     * @param account
     * @return String 用户密码password
     * 通过用户账号查询用户密码
     */
    @Select("select password from user where account=#{account}")
    public String findPasswordByAccount(String account);

    /**
     * @param account
     * @return User列表
     * 通过用户账号获得对应的用户对象
     */
    @Select("select * from user where account = #{account}")
    public List<User> findUserByAccount(String account);
}
