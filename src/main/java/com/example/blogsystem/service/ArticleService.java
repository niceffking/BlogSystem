package com.example.blogsystem.service;

import com.example.blogsystem.dao.ArticleMapper;
import com.example.blogsystem.model.Articleinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public List<Articleinfo> getListByUid(int uid) {
        return articleMapper.getListByUid(uid);
    }
    public int del(Integer aid, int uid) {
        return articleMapper.del(aid, uid);
    }

    public int add(Articleinfo articleinfo) {
        return articleMapper.add(articleinfo);
    }

    public Articleinfo getArticleByid(int aid,int id) {
        return articleMapper.getArticleByid(aid, id);
    }

    public int update(Articleinfo articleinfo) {
        return articleMapper.update(articleinfo);
    }

    public Articleinfo getArticleById(int aid) {
        return articleMapper.getArticleById(aid);
    }

    public Integer artCount(int id) {
        return articleMapper.artCount(id);
    }

    public Integer increment(int id) {
        return articleMapper.increment(id);
    }

    public List<Articleinfo> getListByPage(int psize,int offset) {
        return articleMapper.getListByPage(psize, offset);
    }

    public Integer getCount() {
        return articleMapper.getCount();
    }
}
