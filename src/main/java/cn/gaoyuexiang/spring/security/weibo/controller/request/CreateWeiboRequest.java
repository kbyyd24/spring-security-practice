package cn.gaoyuexiang.spring.security.weibo.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

@Value
public class CreateWeiboRequest {
  String content;

  @JsonCreator
  public CreateWeiboRequest(String content) {
    this.content = content;
  }
}
