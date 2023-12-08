package com.example.blogsystem.dao;

import com.example.blogsystem.model.Articleinfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("select * from articleinfo where uid=#{uid} order by id desc;")
    List<Articleinfo> getListByUid(@Param("uid") int uid);

    @Delete("delete from articleinfo where id = #{aid} and uid = #{uid}")
    int del(@Param("aid")Integer aid, int uid) ;

    @Insert("insert into articleinfo(title,content,uid) values(#{title},#{content},#{uid})")
    int add(Articleinfo articleinfo);

    @Select("select * from articleinfo where id = #{aid} and uid = #{id}")
    Articleinfo getArticleByid(int aid, int id);

    @Update("update  articleinfo set title = #{title}, content=#{content} where id=#{id} and uid=#{uid}")
    int update(Articleinfo articleinfo);

    @Select("select * from articleinfo where id = #{aid}")
    Articleinfo getArticleById(int aid);

    @Select("select count(*) from articleinfo where uid=#{uid}")
    Integer artCount(int uid);

    @Update("update articleinfo set readcount = readcount + 1 where id = #{id}")
    Integer increment(int id);

    @Select("select * from articleinfo order by id desc limit #{psize} offset #{offset}")
    List<Articleinfo>  getListByPage(int psize,int offset);

    @Select("select count(*) from articleinfo")
    Integer getCount();
}
