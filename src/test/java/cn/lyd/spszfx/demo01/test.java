package cn.lyd.spszfx.demo01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.opencv.imgcodecs.Imgcodecs.imread;
@RunWith(SpringRunner.class)
@SpringBootTest
public class test {

    @Test
    public void test1(){
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File("classpath:/resources/images/IMG_20181024_155534.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat testImg = Mat2BufImg.BufImg2Mat(bi,BufferedImage.TYPE_3BYTE_BGR,CvType.CV_8UC3);//imread("classpath:/resources/images/IMG_20181024_155534.jpg");
        HighGui.imshow("test",testImg);
    }

}
