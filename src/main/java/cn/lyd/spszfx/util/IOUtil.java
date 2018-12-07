package cn.lyd.spszfx.util;

import org.opencv.core.Mat;
import org.tensorflow.op.core.Batch;
import static org.opencv.imgcodecs.Imgcodecs.imread;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {

    public static String TEST_IMAGE_PATH_FROM_TEST = "src/test/resources/images/IMG_20181024_155534.jpg";
    public static String IMAGE_PATH_PREFIX = "src/main/resources/static/images/subimages/";
    public static String IMAGE_ROTATED_PATH = "src/main/resources/static/images/rotatedimages/";
    public static String IMAGE_TEMP_PATH = "src/main/resources/static/images/temp/";
    public static String IMAGE_Hough_PATH = "src/main/resources/static/images/hough/";
    public static String IMAGE_SRC_PATH = "src/main/resources/static/images/src/";

    public static String TENSORFLOW_MODEL_PATH = "src/main/resources/static/tensorflow/model";



    public static boolean CreateFile(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }

    public static List<Mat> readBatchImg(String path){
        File dir = new File(path);
        File[] filesList = dir.listFiles();
        List<Mat> imgs = new ArrayList<>();
        for(File f : filesList){
            imgs.add(imread(f.getPath()));
        }
        return imgs;
    }

    public static Mat readImg(String path){
        return imread(path);
    }

}
