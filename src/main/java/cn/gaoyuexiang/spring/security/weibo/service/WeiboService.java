package cn.gaoyuexiang.spring.security.weibo.service;

import cn.gaoyuexiang.spring.security.weibo.model.Weibo;
import cn.gaoyuexiang.spring.security.weibo.repository.WeiboRepository;
import java.util.UUID;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

@Service
public class WeiboService {

  private final WeiboRepository weiboRepository;

  public WeiboService(WeiboRepository weiboRepository) {
    this.weiboRepository = weiboRepository;
  }

  public Weibo create(String content, String ownerId) {
    Weibo weibo = new Weibo();
    weibo.setContent(content);
    weibo.setOwnerId(ownerId);
    weibo.setId(UUID.randomUUID().toString());
    return weiboRepository.save(weibo);
  }

  @PostAuthorize("hasPermission(returnObject, 'READ')")
  public Weibo read(String id) {
    return weiboRepository.findBy(id);
  }
}
