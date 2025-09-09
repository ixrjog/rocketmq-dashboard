package org.apache.rocketmq.dashboard.service.strategy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.auth.authentication.enums.UserType;
import org.apache.rocketmq.dashboard.ldap.service.LdapService;
import org.apache.rocketmq.dashboard.model.User;
import org.apache.rocketmq.remoting.protocol.body.UserInfo;
import org.springframework.stereotype.Service;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/9 13:52
 * &#064;Version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class LdapUserStrategy implements UserStrategy {

    private final LdapService ldapService;

    @Override
    public UserInfo getUserInfoByUsername(String username) {
        User user = ldapService.queryByName(username);
        if (user == null) {
            return null;
        }
        return UserInfo.of(user.getName(), user.getPassword(),
                user.getType() == 0 ? UserType.SUPER.getName() : UserType.NORMAL.getName());
    }

}
