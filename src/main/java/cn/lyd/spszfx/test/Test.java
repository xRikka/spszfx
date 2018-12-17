package cn.lyd.spszfx.test;

import cn.lyd.spszfx.common.IExtraction;
import cn.lyd.spszfx.common.RegionalDetection;
import cn.lyd.spszfx.imgproc.detection.RectRegionalDetection;
import cn.lyd.spszfx.imgproc.detection.edge.CannyEdgeDetector;
import cn.lyd.spszfx.imgproc.extraction.FeatureExtraction;
import cn.lyd.spszfx.model.RGBFeature;
import cn.lyd.spszfx.util.IOUtil;
import cn.lyd.spszfx.util.ImgUtil;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class Test {

    private RegionalDetection regionalDetection;
    private IExtraction featureExtraction;

    public Test(){
        regionalDetection = new RectRegionalDetection(new CannyEdgeDetector());
        featureExtraction = new FeatureExtraction();
    }

    public static void main(String[] args){
        System.load("D:\\IDEAWorkspace\\colorCheckServer\\colorCheckServer\\resources\\opencv\\x64\\opencv_java341.dll");
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Test test = new Test();
        //指定绝对路径
        Mat src = IOUtil.readImg("D:\\IDEAWorkspace\\spszfx\\src\\main\\resources\\static\\images\\src\\IMG_20181217_161811.jpg");
//        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        //        Mat dst = ImgUtil.lightingCompensation(src,32);
//        Mat dst = new Mat();
//        Imgproc.equalizeHist(src,dst);

//        Boolean sign = IOUtil.writeImg("D:\\IDEAWorkspace\\spszfx\\src\\main\\resources\\static\\images\\lightingCompensation\\light_01.jpg",dst);
//        return;

        RGBFeature feature = test.toDo(src,200,30,150);
        List<int[]> RGBList = feature.getRGBList();//所有行的RGB平均值
        for(int[] arr : RGBList){
            System.out.print("[");
            for(int i = 0;i< arr.length - 1;i++){
                System.out.print(arr[i] +" ");
            }
            System.out.println("]");

        }

    }


    private RGBFeature toDo(Mat frame,int minPeakDistance, double loUpDiff, double threshold) {
        Mat roi = regionalDetection.toDo(frame);
        //Mat corrected_roi = horizontalCorrection.degree(roi);
        return featureExtraction.extract(roi, minPeakDistance, loUpDiff, threshold);
    }
}
