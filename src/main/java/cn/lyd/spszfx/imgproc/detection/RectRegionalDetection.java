package cn.lyd.spszfx.imgproc.detection;

import cn.lyd.spszfx.common.RegionalDetection;
import cn.lyd.spszfx.common.EdgeDetector;
import cn.lyd.spszfx.util.ImgUtil;
import org.opencv.core.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("rectRegionalDetection")
public class RectRegionalDetection extends RegionalDetection {

    public RectRegionalDetection(EdgeDetector edgeDetector) {
        super(edgeDetector);
    }
    /**
     * 执行区域提取操作 具体方法
     * @param frame
     * @return
     */
    public Mat toDo(Mat frame) {
        Mat im_rect;//旋转矩形
        Mat im_roi = null;//矫正后矩形
        //图片尺寸统一化
        frame = ImgUtil.img_resize(frame);
        //图片白平衡化处理
        frame = ImgUtil.whiteBalance_grayWorld(frame);
        if(edgeDetector.doDetect(frame)){//轮廓提取
            List<MatOfPoint> contours =  edgeDetector.getContours();
            RotatedRect rotRect = getMaxRotatedRect(contours);//一般最大内面积的连通域就是想要的ROI
            Point[] points = new Point[4];
            rotRect.points(points);
            //Rect rect = rotRect.boundingRect();
            im_rect = Mat.zeros(frame.height(),frame.width(),frame.type());
            frame.copyTo(im_rect);
            //Imgproc.rectangle(im_rect,rect.tl(),rect.br(),new Scalar(0,255,0));
            //绘制旋转矩形框
            ImgUtil.drawROIRect(im_rect,points);
            im_roi = rotatedImgForRect(im_rect,rotRect);//矫正旋转矩形,截取感兴趣区域
            //释放内存
            //im_rect.release();
        }

        writeToLocal();
        return im_roi;
    }

}
