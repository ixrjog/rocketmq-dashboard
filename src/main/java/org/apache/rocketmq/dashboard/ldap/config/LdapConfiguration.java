package org.apache.rocketmq.dashboard.ldap.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/8 16:47
 * &#064;Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ldap", ignoreInvalidFields = true)
public class LdapConfiguration {

    private String url;
    private String base;
    private LdapConfigModel.Manager manager;
    private LdapConfigModel.User user;
    private LdapConfigModel.Group group;
    private LdapConfigModel.RoleMapping roleMapping;

}
