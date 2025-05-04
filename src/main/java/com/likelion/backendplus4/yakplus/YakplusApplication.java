package com.likelion.backendplus4.yakplus;

import com.likelion.backendplus4.yakplus.common.configuration.LogbackConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YakplusApplication {
    public static void main(String[] args) {
        try {
            LogbackConfig logbackConfig = new LogbackConfig();
            logbackConfig.configure();
            SpringApplication.run(YakplusApplication.class, args);
        }catch (Exception e) {
            System.err.println("Error occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
