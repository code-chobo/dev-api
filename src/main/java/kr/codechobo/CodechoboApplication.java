package kr.codechobo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityListeners;

@SpringBootApplication
public class CodechoboApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodechoboApplication.class, args);
    }

}
