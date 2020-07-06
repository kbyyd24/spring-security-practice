package cn.gaoyuexiang.spring.security.weibo.security;

import javax.sql.DataSource;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcAclService;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AclConfig {

  private final DataSource dataSource;

  public AclConfig(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  public Cache cache() {
    return new ConcurrentMapCache("acl");
  }

  @Bean
  public PermissionGrantingStrategy permissionGrantingStrategy() {
    return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
  }

  @Bean
  public AclAuthorizationStrategy aclAuthorizationStrategy() {
    return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("admin"));
  }

  @Bean
  public AclCache aclCache() {
    return new SpringCacheBasedAclCache(
        cache(), permissionGrantingStrategy(), aclAuthorizationStrategy());
  }

  @Bean
  public LookupStrategy lookupStrategy() {
    BasicLookupStrategy basicLookupStrategy = new BasicLookupStrategy(
        dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
    basicLookupStrategy.setAclClassIdSupported(true);
    return basicLookupStrategy;
  }

  @Bean
  public AclService aclService() {
    return new JdbcAclService(dataSource, lookupStrategy());
  }

  @Bean
  public MutableAclService mutableAclService() {
    JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
    jdbcMutableAclService.setClassIdentityQuery("SELECT @@IDENTITY");
    jdbcMutableAclService.setSidIdentityQuery("SELECT @@IDENTITY");
    jdbcMutableAclService.setAclClassIdSupported(true);
    return jdbcMutableAclService;
  }

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setPermissionEvaluator(permissionEvaluator());
    return handler;
  }

  @Bean
  public PermissionEvaluator permissionEvaluator() {
    return new AclPermissionEvaluator(aclService());
  }
}
