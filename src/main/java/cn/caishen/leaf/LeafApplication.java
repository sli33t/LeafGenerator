package cn.caishen.leaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author LB
 */
@SpringBootApplication
@EnableAsync
public class LeafApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeafApplication.class, args);
    }
}
