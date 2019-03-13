package cn.lyd.spszfx.test;

import cn.lyd.spszfx.common.IExtraction;
import cn.lyd.spszfx.common.RegionalDetection;
import cn.lyd.spszfx.imgproc.detection.RectRegionalDetection;
import cn.lyd.spszfx.imgproc.detection.edge.CannyEdgeDetector;
import cn.lyd.spszfx.imgproc.extraction.FeatureExtraction;
import cn.lyd.spszfx.model.RGBFeature;
import cn.lyd.spszfx.util.IOUtil;
import cn.lyd.spszfx.util.ImgUtil;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import javax.sound.sampled.Line;
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
        System.load("E:\\IdeaProjects\\spszfx\\opencv\\x64\\opencv_java341.dll");
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Test test = new Test();
//        System.out.println("\nkernel: 4;anchor: 1:");
//        test.test5(new Size(4,4),new Point(1,1));
//        System.out.println("\nkernel: 9;anchor: 3:");
//        test.test5(new Size(9,9),new Point(3,3));
//        System.out.println("\nkernel: 10;anchor: 4:");
//        test.test5(new Size(10,10),new Point(4,4));
//        System.out.println("\nkernel: 12;anchor: 5:");
//        test.test5(new Size(12,12),new Point(5,5));
//        for(int i = 1;i < 12;i++){
//            System.out.println("\nkernel: 12;anchor: "+i+":");
//            test.test5(new Size(12,12),new Point(i,i));
//        }
        test6();
//        test4();
    }

    public static void test1(){
        Test test = new Test();
        //指定绝对路径
        Mat src = IOUtil.readImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\src\\IMG_20181217_161811.jpg");
//        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGR2GRAY);
        //        Mat dst = ImgUtil.lightingCompensation(src,32);

        //Mat dst = ImgUtil.RGBEqualizeHist(src);

        //Boolean sign = IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\temp\\test_01.jpg",dst);
//       // return;

        RGBFeature feature = test.toDo(src,200,30,150);
        List<int[]> RGBList = feature.getRGBList();//所有行的RGB平均值
        List<Integer> grayList = new ArrayList<>();
        List<Integer> listX = new ArrayList<>();
        for(int[] arr : RGBList){
            int i = 0;
            System.out.print("[");
            for(;i< arr.length - 1;i++){
                System.out.print(arr[i] +" ");
            }
            System.out.print("] ");
            System.out.println("gray = "+ (arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7));


        }
        /*int i = 0;
        int sum = 0;
        int gray = 0;
        for(int[] arr : RGBList) {
            //Gray = (R*38 + G*75 + B*15) >> 7 计算灰度值
            gray = arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7;
            grayList.add(gray);
            sum += gray;
            listX.add(i++);
            //System.out.println(arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7);
        }
        double k;
        double b;
        System.out.println(k =(double) grayList.get(0) / grayList.get(grayList.size()-1));
        RegressionLine line = new RegressionLine(listX,grayList);
        System.out.println("公式：y="+line.getA1()+"x+"+line.getA0());
        System.out.println("误差："+line.getR());
        k = line.getA1();
        b = line.getA0();
        int cx = listX.get(listX.size() / 2);
        int[] cy = RGBList.get(cx);

        int light_arg = sum / listX.size();
        int light_r = 150 - light_arg;*/


        /*for(int[] arr : RGBList){
            System.out.print("[");
            for(int i = 0;i< arr.length - 1;i++){
                System.out.print(arr[i] +" ");
            }
            System.out.println("]");

        }*/

    }

    public static void test2(){
        Mat src = IOUtil.readImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\src\\IMG_20181217_151811.jpg");
        Mat result = ImgUtil.kameans_getLinesMask(src,2,3);
        int i = 0;
        IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\kmeans_test01.jpg",result);
//        System.out.println(src.rows() * src.cols());
    }

    public void test3(){
        List<Mat> srcImgList = IOUtil.readBatchImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\src\\");

        for(int i = 0;i < srcImgList.size();i++){
            Mat mask = ImgUtil.kameans_getLinesMask(ImgUtil.averageBrightness(regionalDetection.toDo(srcImgList.get(i))),2,3);
            IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\kmeans_test_"+i+".jpg",mask);
        }
    }

    public static  void test4(Size kernelSize, Point anchor){
        Mat src = IOUtil.readImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\subimages\\IMG_roi_4.jpg");
        Mat result = ImgUtil.cleanBrightnessEffect(src,kernelSize,anchor);
        int i = 0;
        IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_test_0.jpg",result);
    }

    public void test5(Size kernelSize, Point anchor){
        Mat roi = IOUtil.readImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\subimages\\IMG_roi_0.jpg");
        RGBFeature feature3 = featureExtraction.extract(roi, 200, 30, 150);
        List<int[]> RGBList3 = feature3.getRGBList();//所有行的RGB平均值
        System.out.print("roi  : ");
        for(int[] arr : RGBList3){
            System.out.print((arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7) + ",");
        }
        System.out.println();
        roi = ImgUtil.averageBrightness(roi);
        Mat result = ImgUtil.cleanBrightnessEffect(roi,kernelSize,anchor);
        IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_test_0.jpg",result);
        Mat dst = IOUtil.readImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_test_0.jpg");
        RGBFeature feature = featureExtraction.extract(dst, 200, 30, 150);
        List<int[]> RGBList = feature.getRGBList();//所有行的RGB平均值
        System.out.print("test : ");
        for(int[] arr : RGBList){
            System.out.print((arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7) + ",");
        }
        System.out.println();
        Mat comp = IOUtil.readImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_compensationMat_0.jpg");
        RGBFeature feature1 = featureExtraction.extract(comp, 200, 30, 150);
        System.out.print("comp : ");
        List<int[]> RGBList1 = feature1.getRGBList();//所有行的RGB平均值
        for(int[] arr : RGBList1){
            System.out.print((arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7) + ",");
        }
        System.out.println();
        Mat clea = IOUtil.readImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_reductionMat_0.jpg");
        RGBFeature feature2 = featureExtraction.extract(clea, 200, 30, 150);
        System.out.print("clea : ");
        List<int[]> RGBList2 = feature2.getRGBList();//所有行的RGB平均值
        for(int[] arr : RGBList2){
            System.out.print((arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7) + ",");
        }
    }

    public static void test6(){
        Test test = new Test();
        Mat roi = null;
                List<Mat> srcImgList = IOUtil.readBatchImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\subimages\\");
        for(int i = 0;i < srcImgList.size();i++){
            roi = ImgUtil.averageBrightness(srcImgList.get(i));
            Mat Brightness = ImgUtil.cleanBrightnessEffect(roi,new Size(12,12),new Point(5,5));
            IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\brightness_test_"+i+".jpg",Brightness);

            RGBFeature feature1 = test.featureExtraction.extract(Brightness, 200, 30, 150);
            System.out.print("Brightness "+i+": ");
            List<int[]> RGBList1 = feature1.getRGBList();//所有行的RGB平均值
            for(int[] arr : RGBList1){
                System.out.print((arr[0] * 38 + arr[1] * 75 + arr[2] * 15 >> 7) + ",");
            }
            System.out.println();
            Mat segment = ImgUtil.kameans_getLinesMask(Brightness,2,5);
            IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\kmeans_test_"+i+".jpg",segment);
        }
    }

    private RGBFeature toDo(Mat frame,int minPeakDistance, double loUpDiff, double threshold) {
        Mat roi = regionalDetection.toDo(frame);
        Mat dst = new Mat();
        //Mat corrected_roi = horizontalCorrection.degree(roi);
        //调整整体亮度趋于150
        Imgproc.cvtColor(roi,dst,Imgproc.COLOR_BGR2GRAY);
        Scalar light_avg = Core.mean(dst);
        int r = (int)(150 - light_avg.val[0]);
        Scalar light_r = new Scalar(r,r,r);
        Core.add(roi,light_r,dst);
        //光照补偿
        //dst = ImgUtil.lightingCompensation(dst);//对数变换
        //dst = ImgUtil.lightingCompensation(dst,5);
        return featureExtraction.extract(dst, minPeakDistance, loUpDiff, threshold);
    }
}
