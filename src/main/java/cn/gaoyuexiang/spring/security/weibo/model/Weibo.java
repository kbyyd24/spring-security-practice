package cn.gaoyuexiang.spring.security.weibo.model;

import lombok.Data;

@Data
public class Weibo {

  private String id;
  private String content;
  private String ownerId;
}
