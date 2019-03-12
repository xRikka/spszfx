package cn.lyd.spszfx.demo01;

import org.opencv.core.*;

import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static cn.lyd.spszfx.demo01.ImageController.*;
import static org.opencv.core.CvType.*;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class Demo_byjc {

    /*width:height = 1:3*/
    private static int standard_width_cols = 1200;
    private static int standard_heigth_cols = 1600;
    private static String PRINT_PATH_PREFIX = "E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\subimages\\";
    private static String PRINT_ROTATED_PATH = "E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\rotatedimages\\";
    private static String PRINT_TEMP_PATH = "E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\temp\\";
    public static String PRINT_Hough_PATH = "E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\hough\\";
    private static String ROTATED_IMAGE_SAMPLE_PATH;
    private static String SUB_IMAGE_SAMPLE_PATH;

    Mat test_frame = new Mat();
    Mat grayImage = new Mat();//灰度图
    Mat detectedEdges = new Mat();//边缘提取图
    Mat closedImage = new Mat();//形态学处理边框
    Mat threshImage = new Mat();//二值图
    Mat erodeImage = new Mat();//腐蚀
    Mat dilateImage = new Mat();//膨胀
    Mat temimage = new Mat();//去毛刺
    Mat im_rect = new Mat();//旋转矩形
    Mat im_rotated = new Mat();//矫正后矩形
    List<Mat> sub_images = new ArrayList<>();
    List<Mat> rotated_images = new ArrayList<>();


    private Mat img_negative(Mat image){
        int width = image.width();
        int height = image.height();
        BufferedImage bi = Mat2BufImg.Mat2BufImg(image,".jpg");
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int p = bi.getRGB(i, j);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                p = (a << 24) | (r << 16) | (g << 8) | b;
                bi.setRGB(i, j, p);
            }
        }
        return Mat2BufImg.BufImg2Mat(bi,BufferedImage.TYPE_BYTE_GRAY,CV_8UC1);
    }

    //统一样本图像尺寸
    private Mat img_resize(Mat image){
        Mat new_img = new Mat();
        Imgproc.resize(image,new_img,new Size(standard_width_cols,standard_heigth_cols));
        return new_img;
    }

    private RotatedRect getMaxRotatedRect(List<MatOfPoint> contours){
        List<RotatedRect> rotList = new ArrayList<>();
        for(int i = 0;i < contours.size();i++){
            rotList.add(Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray())));
        }
        int index = 0;
        for(int i = 0;i < rotList.size();i++){
            RotatedRect rotRect = rotList.get(i);
            if(rotList.get(index).size.area() < rotList.get(i).size.area())
                index = i;

        }
        Imgproc.drawContours(test_frame,contours,index,new Scalar(143,188,143),5);
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_test"+System.currentTimeMillis()+".jpg",test_frame);
        return rotList.get(index);
    }

    private Mat getFocusSubImg(Mat img,Point[] points,RotatedRect rotRect){

        int[] x_arr = new int[4];
        int[] y_arr = new int[4];
        for(int i = 0;i < points.length;i++){
            x_arr[i] = (int)points[i].x;
            y_arr[i] = (int)points[i].y;
        }
        //x，y坐标集分别由大到小排序
        Arrays.sort(x_arr);
        Arrays.sort(y_arr);
        int width = x_arr[x_arr.length - 1] - x_arr[0];
        int height = y_arr[y_arr.length - 1] - y_arr[0];
        //Mat sub_img = img.submat(new Range(x_arr[0],x_arr[x_arr.length - 1]),new Range(y_arr[0],y_arr[y_arr.length - 1]));
        Mat sub_img = new Mat(img,new Rect(x_arr[0],y_arr[0],width,height));
        sub_images.add(sub_img);
        //drawFocusRect(img,points);
        //sub_images.add(img);
        return sub_img;
    }

    //绘制旋转矩形
    private void drawFocusRect(Mat image,Point[] points){
        Imgproc.line(image,points[0],points[1],new Scalar(0,255,0),3);
        Imgproc.line(image,points[1],points[2],new Scalar(0,255,0),3);
        Imgproc.line(image,points[2],points[3],new Scalar(0,255,0),3);
        Imgproc.line(image,points[3],points[0],new Scalar(0,255,0),3);
        //imwrite("images_result/IMG_"+System.currentTimeMillis()+".jpg",image);
    }

   /* private double CalcDegree(Mat srcImg,Mat dstImg){
        Imgproc.HoughLines();
    }*/

    private Mat rotatedImgForRect(Mat img,RotatedRect rotRect){
        Point centor = new Point(img.width() / 2,img.height() / 2);//图像中心点
        Point[] points = new Point[4];
        Mat rotatedMat = new Mat();
        rotRect.points(points);
        double rot_angle = rotRect.angle;
        if(rotRect.angle < -45.0){
            rot_angle = 90+rotRect.angle;
        }
        //根据angle以矩形中心点为轴心旋转原图进行角度纠正
        img.copyTo(rotatedMat);
        Mat affineTrans = Imgproc.getRotationMatrix2D(centor,rot_angle,1);
        Imgproc.warpAffine(img,rotatedMat,affineTrans,rotatedMat.size(),Imgproc.INTER_NEAREST);
        //Imgproc.line(rotatedMat,new Point(centor.x-10,centor.y),new Point(centor.x+10,centor.y),new Scalar(0,255,255),5);//中心绘点
        //图像计算旋转后新坐标
        double cosv = Math.cos(Math.PI / 180.0 * rot_angle);
        double sinv = Math.sin(Math.PI / 180.0 * rot_angle);
        Point[] new_points = new Point[4];
        double[] new_p = new double[2];
        double py;
        double cy;
        //计算矫正后矩形的新的四个端点坐标
        for(int i = 0;i < 4;i++){
            py = img.rows() - points[i].y;
            cy = img.rows() - centor.y;
            new_p[0] = (points[i].x - centor.x) * cosv - (py - cy) * sinv + centor.x;
            new_p[1] = img.rows() - ((points[i].x - centor.x) * sinv + (py- cy) * cosv + cy);
            new_points[i] = new Point(new_p);
        }

        rotated_images.add(rotatedMat);
        getFocusSubImg(rotatedMat,new_points,rotRect);
        return rotatedMat;
    }

    private Mat doCanny(Mat frame) {

        Mat kernel;//形态学内核
        double threshold = 5;
        //图片尺寸统一化
        frame = img_resize(frame);
        frame.copyTo(test_frame);
        // 灰度化
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);
        // 滤波降噪
            // 均值
        //Imgproc.blur(grayImage, detectedEdges, new Size(9, 9));
            // 双边
        //Mat sbImage = new Mat();
        //Imgproc.bilateralFilter(grayImage,detectedEdges,25,25*2,25/2);
            //中值
        Imgproc.medianBlur(grayImage,detectedEdges,9);
        // Canny算子边缘检测
        Imgproc.Canny(detectedEdges, detectedEdges, threshold, threshold * 3);
        //二值化
        Imgproc.threshold(detectedEdges,threshImage,100, 255,Imgproc.THRESH_BINARY);
        //形态学操作
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(10,10));
        //保边
        Imgproc.morphologyEx(threshImage,closedImage,Imgproc.MORPH_GRADIENT,kernel);
        //去小点，暂时没作用
        Imgproc.morphologyEx(closedImage,closedImage,Imgproc.MORPH_CLOSE,kernel);

        //腐蚀与膨胀，iterator = 4
        Imgproc.erode(closedImage,erodeImage,kernel);
        Imgproc.dilate(erodeImage,dilateImage,kernel);
        //Imgproc.dilate(closedImage,dilateImage,kernel);
        printImg();
        //区域填充
        temimage = Mat.zeros(dilateImage.height(),dilateImage.width(), dilateImage.type());
        Mat temp = new Mat(dilateImage.height()+2,dilateImage.width()+2, dilateImage.type());
        Mat mask = new Mat(temp.height()+2,temp.width()+2, temp.type(),new Scalar(0));
        dilateImage.copyTo(temp.submat(new Range(1,dilateImage.height() + 1), new Range(1,dilateImage.width() + 1)));
        Imgproc.floodFill(temp,mask,new Point(0,0), new Scalar(255));//大背景漫水填充为白色
        Core.bitwise_not(temp,temp);//二值图反转
            //二值图与阈值图相加合并，获得蒙版
        Core.bitwise_or(temp.submat(new Range(1,dilateImage.height() + 1), new Range(1,dilateImage.width() + 1)),dilateImage,temimage);
            //蒙版再次腐蚀，去除边缘毛刺
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(100,100));
        Imgproc.erode(temimage,temimage,kernel);
        //定位轮廓
        Mat im = new Mat();
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        temimage.copyTo(im);
        Imgproc.findContours(im,contours,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        RotatedRect rotRect = getMaxRotatedRect(contours);

        Point[] points = new Point[4];
        rotRect.points(points);
//        Rect rect = rotRect.boundingRect();
        im_rect = Mat.zeros(frame.height(),frame.width(),frame.type());
        frame.copyTo(im_rect);
        //Imgproc.rectangle(im_rect,rect.tl(),rect.br(),new Scalar(0,255,0));
        //绘制旋转矩形框
        //drawFocusRect(im_rect,points);
        im_rotated = rotatedImgForRect(im_rect,rotRect);
        return im_rotated;
    }

    public void printImg(){
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_temimage.jpg",temimage);
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_gray.jpg",grayImage);
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_detectedEdges.jpg",detectedEdges);
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_closed.jpg",closedImage);
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_thresh.jpg",threshImage);
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_erode.jpg",erodeImage);
        imwrite(PRINT_TEMP_PATH+"IMG_TEMP_dilateImage.jpg",dilateImage);

    }

    public void writeToLocal(){
        long currentTime = 0L;
        for(int i = 0;i < rotated_images.size();i++){
            currentTime = System.currentTimeMillis();
            imwrite(PRINT_ROTATED_PATH+"IMG_"+i+".jpg",rotated_images.get(i));
            imwrite(PRINT_PATH_PREFIX+"IMG_SUB_"+i+".jpg",sub_images.get(i));
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Demo_byjc byjc = new Demo_byjc();
        File dir = new File("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\src");
        File[] filesList = dir.listFiles();
        for(File f : filesList){
            byjc.doCanny(imread(f.getPath()));
        }
        byjc.writeToLocal();
        HorizontalCorrection.HoughLinesDegree(byjc.sub_images.get(0),new Mat());
        Mat img = imread("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\hough\\HOUGH_DEGREE.jpg");
        FeatureExtraction fe = new FeatureExtraction(img);
        fe.LocalPeakOfFeature(200,30,150);

    }

}
