package com.example.blogsystem.model.vo;

import com.example.blogsystem.model.Userinfo;
import lombok.Data;

@Data
public class UserinfoVO extends Userinfo {

    // 验证码
    private String checkCode;

    // 用户发表的文章总数
    private int artCount;
}
