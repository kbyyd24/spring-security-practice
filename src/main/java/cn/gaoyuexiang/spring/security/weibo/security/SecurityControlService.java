package cn.gaoyuexiang.spring.security.weibo.security;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityControlService {

  private final MutableAclService mutableAclService;

  public SecurityControlService(MutableAclService mutableAclService) {
    this.mutableAclService = mutableAclService;
  }

  public void onCreate(Class<?> domainClass, String id, Authentication principal) {
    ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(domainClass, id);
    PrincipalSid sid = new PrincipalSid(principal);
    GrantedAuthoritySid userSid = new GrantedAuthoritySid("user");
    MutableAcl acl = findOrCreate(objectIdentity);
    acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, sid, true);
    acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
    acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
    acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, true);
    acl.insertAce(acl.getEntries().size(), BasePermission.READ, userSid, true);
    mutableAclService.updateAcl(acl);
  }

  private MutableAcl findOrCreate(ObjectIdentity objectIdentity) {
    try {
      return (MutableAcl) mutableAclService.readAclById(objectIdentity);
    } catch (NotFoundException e) {
      return mutableAclService.createAcl(objectIdentity);
    }
  }

  private Long parseId(String id) {
    return id.chars().mapToLong(value -> (long) value).sum();
  }
}
