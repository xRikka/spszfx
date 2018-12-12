package cn.lyd.spszfx;

import cn.lyd.spszfx.initializer.ApplicationStartInitializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("cn.lyd.spszfx.mapper")
@ComponentScan(basePackages = "cn.lyd.spszfx")
public class SpszfxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpszfxApplication.class, args);
    }
}
