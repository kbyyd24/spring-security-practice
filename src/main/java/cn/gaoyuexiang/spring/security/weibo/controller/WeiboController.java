package cn.gaoyuexiang.spring.security.weibo.controller;

import cn.gaoyuexiang.spring.security.weibo.controller.request.CreateWeiboRequest;
import cn.gaoyuexiang.spring.security.weibo.model.Weibo;
import cn.gaoyuexiang.spring.security.weibo.usecase.WeiboUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("weibos")
public class WeiboController {
  private final WeiboUseCase weiboUseCase;

  public WeiboController(WeiboUseCase weiboUseCase) {
    this.weiboUseCase = weiboUseCase;
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody CreateWeiboRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String weiboId = weiboUseCase.create(request, authentication);
    return ResponseEntity.created(
            UriComponentsBuilder.fromPath("/weibos/" + weiboId).build().toUri())
        .build();
  }

  @GetMapping("{id}")
  public Weibo read(@PathVariable("id") String id) {
    return weiboUseCase.find(id);
  }
}
