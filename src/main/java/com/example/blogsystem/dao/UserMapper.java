package com.example.blogsystem.dao;

import com.example.blogsystem.model.Userinfo;
import com.example.blogsystem.model.vo.UserinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    int reg(Userinfo userinfo);

    @Select("select * from userinfo where username = #{username}")
    Userinfo getUserByName(String username);

    @Select("select * from userinfo where id=#{id}")
    UserinfoVO getUserById(int id);
}
