package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.jwt.JsonWebTokenUtility;



@SpringBootTest
public class JsonWebTokenUtilityTests {
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @Test
    public void testCreateToken() {
        String token = jsonWebTokenUtility.createToken("hahahahahaah");
        System.out.println("token="+token);
    }
}
