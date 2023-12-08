package com.example.blogsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Articleinfo implements Serializable {
    private int id;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime createtime;
    private LocalDateTime updatetime;

    private int uid;
    private int readcount;
    private int state;
}


