package cn.lyd.spszfx.demo01;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static cn.lyd.spszfx.demo01.ImageController.TEST_IMAGE_PATH_FROM_TEST;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;


public class OpenCVTest {

    public static void ROITest1(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = imread(TEST_IMAGE_PATH_FROM_TEST);
        /*Mat img = null;
        try {
            img = Mat2BufImg.BufImg2Mat(ImageIO.read(new File(TEST_IMAGE_PATH_FROM_TEST)),BufferedImage.TYPE_3BYTE_BGR,CV_8UC3);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Mat mask = Mat.zeros(img.size(),CV_8UC1);
        Rect rect = new Rect(100,100,100,100);
        mask.submat(rect);
        mask.setTo(new Scalar(255));
        Mat img2 = new Mat();
        img.copyTo(img2,mask);
        imwrite("IMG_RE_01.jpg",img2);
    }

    public static void main(String[] args){
        ROITest1();
    }
}
