package com.filepan.mapper;

import com.filepan.entity.Priv;
import com.filepan.entity.Role;
import com.filepan.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 根据账号密码进行登录
     */
    @Select("select * from user where user_name = #{user_name} and user_pwd = #{user_pwd};")
    User login(@Param("user_name") String user_name, @Param("user_pwd") String user_pwd);

    /**
     * 注册 插入账号信息
     */
    @Insert("INSERT INTO user(user_name, user_pwd, role_id) VALUES(#{user_name}, #{user_pwd}, #{role_id})")
    int insert(@Param("user_name") String user_name, @Param("user_pwd") String user_pwd, @Param("role_id") int role_id);
    /**
     * 根据用户 id 查询权限
     */
    @Select("select * from priv where role_id = (select role_id from user where id = #{id})")
    List<Priv> selectPriv(@Param("id") int id);

    /**
     * 根据用户 id 查询角色
     */
    @Select("select * from role where role_id = (select role_id from user where id = #{id})")
    Role selectRole(@Param("id") int id);
}
