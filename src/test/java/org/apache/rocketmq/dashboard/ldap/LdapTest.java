package org.apache.rocketmq.dashboard.ldap;

import jakarta.annotation.Resource;
import org.apache.rocketmq.dashboard.BaseTest;
import org.apache.rocketmq.dashboard.ldap.config.LdapConfiguration;
import org.apache.rocketmq.dashboard.ldap.model.LdapPerson;
import org.apache.rocketmq.dashboard.ldap.service.LdapService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/8 16:47
 * &#064;Version 1.0
 */
@SpringBootTest
public class LdapTest extends BaseTest {

    @Resource
    private LdapConfiguration ldapConfiguration;

    @Resource
    private org.apache.rocketmq.dashboard.ldap.client.LdapClient ldapClient;

    @Resource
    private LdapService ldapService;

    @Test
    void encryptTest() {
        System.out.println(ldapConfiguration);
    }

    @Test
    void findPersonByDnTest() {
        String dn = "cn=baiyi,ou=People";
        LdapPerson.Person person = ldapClient.findPersonByDn(dn);
        System.out.println(person);
    }

    @Test
    void queryByUsernameAndPasswordTest() {
        System.out.println(ldapService.queryByUsernameAndPassword("baiyi","XXXXX"));
    }

}