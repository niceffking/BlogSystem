package com.example.blogsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Userinfo implements Serializable /*具备序列化和反序列化的功能*/ {
    private int id;
    private String username;
    private String password;
    private String photo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
    private int state;
}
