package org.apache.rocketmq.dashboard.ldap.client;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.dashboard.ldap.config.LdapConfiguration;
import org.apache.rocketmq.dashboard.ldap.mapper.PersonAttributesMapper;
import org.apache.rocketmq.dashboard.ldap.model.LdapPerson;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/8 16:57
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class LdapClient {

    @Resource
    private LdapConfiguration ldapConfiguration;

    public LdapPerson.Person findPersonByDn(String dn) {
        return buildLdapTemplate().lookup(dn, new PersonAttributesMapper());
    }

    public LdapTemplate buildLdapTemplate() {
        LdapContextSource contextSource = buildLdapContextSource();
        TransactionAwareContextSourceProxy sourceProxy = buildTransactionAwareContextSourceProxy(contextSource);
        return new LdapTemplate(sourceProxy);
    }

    private LdapContextSource buildLdapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapConfiguration.getUrl());
        contextSource.setBase(ldapConfiguration.getBase());
        contextSource.setUserDn(ldapConfiguration.getManager()
                .getDn());
        contextSource.setPassword(ldapConfiguration.getManager()
                .getPassword());
        contextSource.setPooled(true);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    private TransactionAwareContextSourceProxy buildTransactionAwareContextSourceProxy(
            LdapContextSource contextSource) {
        return new TransactionAwareContextSourceProxy(contextSource);
    }

}
