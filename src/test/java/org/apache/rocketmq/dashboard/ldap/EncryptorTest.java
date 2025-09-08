package org.apache.rocketmq.dashboard.ldap;

import jakarta.annotation.Resource;
import org.apache.rocketmq.dashboard.BaseTest;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/8 16:38
 * &#064;Version 1.0
 */
@SpringBootTest
public class EncryptorTest extends BaseTest {

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    void encryptTest() {
        String str = stringEncryptor.encrypt("123456");
        System.out.println(str);
    }

}