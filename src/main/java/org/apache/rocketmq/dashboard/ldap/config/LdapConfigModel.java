package org.apache.rocketmq.dashboard.ldap.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/8 16:46
 * &#064;Version 1.0
 */
public class LdapConfigModel {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Manager {
        private String dn;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String id;
        private String dn;
        private String objectClass;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Group {
        private String id;
        private String dn;
        private String objectClass;
        private String memberAttribute;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleMapping {
        private List<String> superGroups;
        private List<String> userGroups;
    }

}
