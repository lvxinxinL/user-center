package com.example.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 测试加密算法
     */
    @Test
    void testDigest() {
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(("GHOST" + "myPassword").getBytes());
        System.out.println(md5DigestAsHex);
    }


}
