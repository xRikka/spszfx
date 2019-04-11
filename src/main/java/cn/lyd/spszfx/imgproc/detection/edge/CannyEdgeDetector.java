package cn.lyd.spszfx.imgproc.detection.edge;

import cn.lyd.spszfx.common.EdgeDetector;
import cn.lyd.spszfx.util.ImgUtil;
import cn.lyd.spszfx.util.IOUtil;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

@Component
public class CannyEdgeDetector extends EdgeDetector {
    //相关参数
    private double edge_threshold = 7;//边缘检测算子参数
    private int blur_size = 9;//平滑滤波 内核尺寸
    private int ksize = 10;//形态学内核尺寸
    private int count = 0;

    /*public CannyEdgeDetector(Mat src){
        this.src = src;
    }*/



    @Override
    public boolean doDetect(Mat frame) {
        contours.clear();
        Mat grayImage = new Mat();//灰度图
        Mat detectedEdges = new Mat();//边缘提取图
        Mat closedImage = new Mat();//形态学处理边框
        Mat threshImage = new Mat();//二值图
        Mat erodeImage = new Mat();//腐蚀
        Mat dilateImage = new Mat();//膨胀
        Mat temimage;//去毛刺
        Mat kernel;//形态学内核
        Mat temp;
        Mat mask;
        // 灰度化
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);
        //直方图均衡化
        //Imgproc.equalizeHist(grayImage,grayImage);
        //光照补偿
        //grayImage = ImgUtil.lightingCompensation(grayImage,32);
        // 滤波降噪
        // 均值
        //Imgproc.blur(grayImage, detectedEdges, new Size(9, 9));
        // 双边
        //Mat sbImage = new Mat();
        //Imgproc.bilateralFilter(grayImage,detectedEdges,25,25*2,25/2);
        //中值
        Imgproc.medianBlur(grayImage,grayImage,blur_size);
        //高斯
        //Imgproc.GaussianBlur(grayImage,grayImage,new Size(blur_size,blur_size),0);
        // Canny算子边缘检测
        Imgproc.Canny(grayImage, detectedEdges, edge_threshold, edge_threshold * 3);
        //二值化
        Imgproc.threshold(detectedEdges,threshImage,ImgUtil.BINARY_THRESHOLD, 255,Imgproc.THRESH_BINARY);
        //形态学操作
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(ksize,ksize));
        //保边
        Imgproc.morphologyEx(threshImage,closedImage,Imgproc.MORPH_GRADIENT,kernel);
        //去小点
        Imgproc.morphologyEx(closedImage,closedImage,Imgproc.MORPH_CLOSE,kernel);
        //腐蚀与膨胀，iterator = 4
        Imgproc.erode(closedImage,erodeImage,kernel);
        Imgproc.dilate(erodeImage,dilateImage,kernel);
        //区域填充
        temimage = Mat.zeros(dilateImage.height(),dilateImage.width(), dilateImage.type());
        temp = new Mat(dilateImage.height()+2,dilateImage.width()+2, dilateImage.type());
        mask = new Mat(temp.height()+2,temp.width()+2, temp.type(),new Scalar(0));
        dilateImage.copyTo(temp.submat(new Range(1,dilateImage.height() + 1), new Range(1,dilateImage.width() + 1)));
        Imgproc.floodFill(temp,mask,new Point(0,0), new Scalar(255));//大背景漫水填充为白色
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_quyu1_"+count+".jpg",temp);
        Core.bitwise_not(temp,temp);//二值图反转
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_quyu2_"+count+".jpg",temp);
        //二值图与阈值图相加合并，获得蒙版
        Core.bitwise_or(temp.submat(new Range(1,dilateImage.height() + 1), new Range(1,dilateImage.width() + 1)),dilateImage,temimage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_quyu3_"+count+".jpg",temimage);
        //蒙版再次腐蚀，去除边缘毛刺
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(ksize * 10,ksize * 10));
        Imgproc.erode(temimage,temimage,kernel);
        //定位轮廓
        //temimage.copyTo(temp);
        Imgproc.findContours(temimage,contours,mask,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        //释放内存
        /*
        grayImage.release();
        detectedEdges.release();
        closedImage.release();
        threshImage.release();
        erodeImage.release();
        temimage.release();
        kernel.release();
        temp.release();
        mask.release();
*/
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_frame"+count+".jpg",frame);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_temimage"+count+".jpg",temimage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_gray"+count+".jpg",grayImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_detectedEdges"+count+".jpg",detectedEdges);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_closed"+count+".jpg",closedImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_thresh"+count+".jpg",threshImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_erode"+count+".jpg",erodeImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_dilateImage"+count+".jpg",dilateImage);
        count++;
        //writeImg();//输出过程img
        if(contours.size() > 0){
            return true;
        }
        return false;
    }


/*
    private void writeImg(){
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_temimage"+count+".jpg",temimage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_gray"+count+".jpg",grayImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_detectedEdges"+count+".jpg",detectedEdges);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_closed"+count+".jpg",closedImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_thresh"+count+".jpg",threshImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_erode"+count+".jpg",erodeImage);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_dilateImage"+count+".jpg",dilateImage);
        count++;
    }
    */
}
