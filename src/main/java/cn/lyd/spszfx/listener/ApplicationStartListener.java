package cn.lyd.spszfx.listener;

import org.opencv.core.Core;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            System.out.println("初始化OpenCV... ...");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("初始化OpenCV结束");
        }

    }

}
