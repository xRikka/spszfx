package cn.lyd.spszfx.initializer;

import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;


public class ApplicationStartInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartInitializer.class);
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        logger.info("初始化OpenCV... ...");
        //System.load("E:\\IdeaProjects\\spszfx\\opencv\\x64\\opencv_java341.dll");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("初始化OpenCV结束!");
    }
}
