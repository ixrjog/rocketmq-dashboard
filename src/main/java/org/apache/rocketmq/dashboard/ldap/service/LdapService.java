package org.apache.rocketmq.dashboard.ldap.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.dashboard.ldap.client.LdapClient;
import org.apache.rocketmq.dashboard.ldap.config.LdapConfiguration;
import org.apache.rocketmq.dashboard.ldap.model.LdapPerson;
import org.apache.rocketmq.dashboard.model.User;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.apache.rocketmq.dashboard.ldap.service.LdapService.SEARCH_KEY.OBJECTCLASS;


@Slf4j
@Service
public class LdapService {

    public interface SEARCH_KEY {
        String OBJECTCLASS = "objectclass";
    }

    @Resource
    private LdapConfiguration ldapConfiguration;

    @Resource
    private LdapClient ldapClient;

    public User queryByName(String name) {
        String userDN = toUserDN(name);
        LdapPerson.Person person = ldapClient.findPersonByDn(userDN);
        if (person == null) {
            return null;
        }
        // 用户所有角色
        List<String> userRoles = searchLdapGroup(name);
        boolean isSuperUser = userRoles.stream()
                .anyMatch(role -> ldapConfiguration.getRoleMapping()
                        .getSuperGroups()
                        .contains(role));
        if (isSuperUser) {
            return new User(name, "", User.SUPER);
        }
        boolean isNormalUser = userRoles.stream()
                .anyMatch(role -> ldapConfiguration.getRoleMapping()
                        .getUserGroups()
                        .contains(role));
        if (isNormalUser) {
            return new User(name, "", User.NORMAL);
        }
        // 未匹配到角色禁止登录
        return null;
    }

    private String toUserDN(String username) {
        String userDN = ldapConfiguration.getUser()
                .getDn();
        if (StringUtils.hasText(userDN)) {
            return ldapConfiguration.getUser()
                    .getId() + "=" + username + "," + ldapConfiguration.getUser()
                    .getDn();
        } else {
            return ldapConfiguration.getUser()
                    .getId() + "=" + username;
        }
    }

    private String toGroupDN(String groupName) {
        String groupDN = ldapConfiguration.getGroup()
                .getDn();
        if (StringUtils.hasText(groupDN)) {
            return ldapConfiguration.getGroup()
                    .getId() + "=" + groupName + "," + ldapConfiguration.getGroup()
                    .getDn();
        } else {
            return ldapConfiguration.getGroup()
                    .getId() + "=" + groupName;
        }
    }

    public User queryByUsernameAndPassword(String username, String password) {
        boolean verify = verifyLogin(username, password);
        if (!verify) {
            return null;
        }
        return queryByName(username);
    }

    public boolean verifyLogin(String username, String password) {
        log.info("Verify login content username={}", username);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(OBJECTCLASS, "person"))
                .and(new EqualsFilter(ldapConfiguration.getUser()
                        .getId(), username));
        try {
            return ldapClient.buildLdapTemplate()
                    .authenticate(ldapConfiguration.getUser()
                            .getDn(), filter.toString(), password);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private List<String> searchLdapGroup(String username) {
        List<String> groupList = Lists.newArrayList();
        try {
            String groupBaseDN = ldapConfiguration.getGroup()
                    .getDn();
            String groupMember = ldapConfiguration.getGroup()
                    .getMemberAttribute();
            String userId = ldapConfiguration.getUser()
                    .getId();
            String userDn = toUserDN(username);
            String userFullDn = Joiner.on(",")
                    .skipNulls()
                    .join(userDn, ldapConfiguration.getBase());
            groupList = ldapClient.buildLdapTemplate()
                    .search(LdapQueryBuilder.query()
                            .base(groupBaseDN)
                            .where(groupMember)
                            .is(userFullDn)
                            .and(userId)
                            .like("*"), (AttributesMapper<String>) attributes -> attributes.get(userId)
                            .get(0)
                            .toString());
        } catch (Exception e) {
            log.warn("Search ldap group error: username={}, {}", username, e.getMessage());
        }
        return groupList;
    }

}
