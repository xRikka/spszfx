package cn.lyd.spszfx.common;

import cn.lyd.spszfx.util.IOUtil;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public abstract class RegionalDetection implements IDetection{

    protected EdgeDetector edgeDetector;
    protected List<Mat> roi_images;//保存截取得到的感兴趣区域
    protected List<Mat> rotated_images;//保存矫正后的图像

    public RegionalDetection() {
        this.edgeDetector = null;
        this.roi_images = new ArrayList<>();
        this.rotated_images = new ArrayList<>();
    }

    public RegionalDetection(EdgeDetector edgeDetector) {
        this.edgeDetector = edgeDetector;
        this.roi_images = new ArrayList<>();
        this.rotated_images = new ArrayList<>();
    }
    /**
     * 获取对应轮廓的最大面积旋转矩形
     * @param contours
     * @return
     */
    protected RotatedRect getMaxRotatedRect(List<MatOfPoint> contours){
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
        /*Imgproc.drawContours(test_frame,contours,index,new Scalar(143,188,143),5);
        imwrite(IOUtil.IMAGE_TEMP_PATH+"IMG_TEMP_test.jpg",test_frame);*/
        return rotList.get(index);
    }
    /**
     * 获取感兴趣区域
     * @param img
     * @param points
     * @param rotRect
     * @return
     */
    protected Mat cutROIImg(Mat img, Point[] points, RotatedRect rotRect){

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
        //Mat roi_img = img.submat(new Range(x_arr[0],x_arr[x_arr.length - 1]),new Range(y_arr[0],y_arr[y_arr.length - 1]));
        Mat roi_img = new Mat(img,new Rect(x_arr[0],y_arr[0],width,height));
        roi_images.add(roi_img);
        //drawFocusRect(img,points);
        //roi_images.add(img);
        return roi_img;
    }
    /**
     * 将旋转矩形矫正
     * @param img
     * @param rotRect
     * @return
     */
    protected Mat rotatedImgForRect(Mat img,RotatedRect rotRect){
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
        cutROIImg(rotatedMat,new_points,rotRect);
        return rotatedMat;
    }
    /**
     * 执行区域提取操作 具体方法
     * @param frame
     * @return
     */
    public abstract Mat toDo(Mat frame);
    /**
     * 图像保存到磁盘
     */
    public void writeToLocal(){
        long currentTime = 0L;
        for(int i = 0;i < rotated_images.size();i++){
            currentTime = System.currentTimeMillis();
            imwrite(IOUtil.IMAGE_ROTATED_PATH+"IMG_"+i+".jpg",rotated_images.get(i));
            imwrite(IOUtil.IMAGE_PATH_PREFIX+"IMG_roi_"+i+".jpg",roi_images.get(i));
        }
    }

    public EdgeDetector getEdgeDetector() {
        return edgeDetector;
    }

    public void setEdgeDetector(EdgeDetector edgeDetector) {
        this.edgeDetector = edgeDetector;
    }

    public List<Mat> getRoi_images() {
        return roi_images;
    }

    public void setRoi_images(List<Mat> roi_images) {
        this.roi_images = roi_images;
    }

    public List<Mat> getRotated_images() {
        return rotated_images;
    }

    public void setRotated_images(List<Mat> rotated_images) {
        this.rotated_images = rotated_images;
    }

}
