package cn.gaoyuexiang.spring.security.weibo.repository;

import cn.gaoyuexiang.spring.security.weibo.model.Weibo;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WeiboRepository {

  Weibo save(Weibo weibo);

  Weibo findBy(String id);

}
