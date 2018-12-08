package cn.lyd.spszfx.initializer;

import org.opencv.core.Core;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationStartInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("初始化OpenCV... ...");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("初始化OpenCV结束");
    }
}
