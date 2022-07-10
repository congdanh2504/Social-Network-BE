package com.example.social_network_fpt_be;

import com.example.social_network_fpt_be.cloudStorage.connectFirebase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@SpringBootApplication
public class SocialNetworkFptBeApplication {

    public static void main(String[] args){
        SpringApplication.run(SocialNetworkFptBeApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
