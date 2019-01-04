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

import java.util.ArrayList;
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
        Mat src = IOUtil.readImg("D:\\IDEAWorkspace\\spszfx\\src\\main\\resources\\static\\images\\src\\IMG_20181101_215111.jpg");
//        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        //        Mat dst = ImgUtil.lightingCompensation(src,32);
//        Mat dst = new Mat();
        //Mat dst = ImgUtil.RGBEqualizeHist(src);

        //Boolean sign = IOUtil.writeImg("D:\\IDEAWorkspace\\spszfx\\src\\main\\resources\\static\\images\\temp\\test_01.jpg",dst);
//       // return;

        RGBFeature feature = test.toDo(src,200,30,150);
        List<int[]> RGBList = feature.getRGBList();//所有行的RGB平均值
        List<Integer> grayList = new ArrayList<>();
        List<Integer> listX = new ArrayList<>();
        int i = 0;
        for(int[] arr : RGBList) {
            //Gray = (R*38 + G*75 + B*15) >> 7 计算灰度值
            grayList.add(arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7);
            listX.add(i++);
            //System.out.println(arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7);
        }
        double k;
        System.out.println(k =(double) grayList.get(0) / grayList.get(grayList.size()-1));
        RegressionLine line = new RegressionLine(listX,grayList);
        System.out.println("公式：y="+line.getA1()+"x+"+line.getA0());
        System.out.println("误差："+line.getR());
        /*LinearRegression lr = new LinearRegression(grayList,0.00001,100);
        lr.lineGre();
        System.out.println(lr.predict(1));*/
        /*for(int[] arr : RGBList){
            System.out.print("[");
            for(int i = 0;i< arr.length - 1;i++){
                System.out.print(arr[i] +" ");
            }
            System.out.println("]");

        }*/

    }


    private RGBFeature toDo(Mat frame,int minPeakDistance, double loUpDiff, double threshold) {
        Mat roi = regionalDetection.toDo(frame);
        //Mat corrected_roi = horizontalCorrection.degree(roi);
        return featureExtraction.extract(roi, minPeakDistance, loUpDiff, threshold);
    }
}
