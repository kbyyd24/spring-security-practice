package cn.gaoyuexiang.spring.security.weibo.usecase;

import cn.gaoyuexiang.spring.security.weibo.controller.request.CreateWeiboRequest;
import cn.gaoyuexiang.spring.security.weibo.model.Weibo;
import cn.gaoyuexiang.spring.security.weibo.security.SecurityControlService;
import cn.gaoyuexiang.spring.security.weibo.service.WeiboService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeiboUseCase {
  private final WeiboService weiboService;
  private final SecurityControlService securityControlService;

  public WeiboUseCase(WeiboService weiboService, SecurityControlService securityControlService) {
    this.weiboService = weiboService;
    this.securityControlService = securityControlService;
  }

  @Transactional
  public String create(CreateWeiboRequest request, Authentication authentication) {
    Weibo weibo =
        weiboService.create(
            request.getContent(), ((UserDetails) authentication.getPrincipal()).getUsername());
    securityControlService.onCreate(weibo.getClass(), weibo.getId(), authentication);
    return weibo.getId();
  }

  public Weibo find(String id) {
    return weiboService.read(id);
  }
}
