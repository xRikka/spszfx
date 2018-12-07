package cn.lyd.spszfx.util;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class ImgUtil {


    public static int BINARY_THRESHOLD = 100;//二值化阈值
    public static int STANDARD_WIDTH_COLS = 1200;//width:height = 1:3
    public static int STANDARD_HEIGHT_ROWS = 1600;

    /**
     * 颜色分量转换为RGB值
     *
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }
    /**
     * 绘制旋转矩形
     * @param image
     * @param points
     * @return
     */
    public static Mat drawROIRect(Mat image, Point[] points){
        Mat temp = new Mat();
        image.copyTo(temp);
        Imgproc.line(temp,points[0],points[1],new Scalar(0,255,0),3);
        Imgproc.line(temp,points[1],points[2],new Scalar(0,255,0),3);
        Imgproc.line(temp,points[2],points[3],new Scalar(0,255,0),3);
        Imgproc.line(temp,points[3],points[0],new Scalar(0,255,0),3);
        imwrite("images_result/IMG_"+System.currentTimeMillis()+".jpg",temp);
        return temp;
    }
    /**
     * 图像色彩反转
     * @param image
     * @return
     */
    public static Mat img_negative(Mat image){
        int width = image.width();
        int height = image.height();
        BufferedImage bi = TypeConversionUtil.Mat2BufImg(image,".jpg");
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
        return TypeConversionUtil.BufImg2Mat(bi,BufferedImage.TYPE_BYTE_GRAY,CV_8UC1);
    }
    /**
     * 统一样本图像尺寸
     * @param image
     * @return
     */
    public static Mat img_resize(Mat image){
        Mat new_img = new Mat();
        Imgproc.resize(image,new_img,new Size(STANDARD_WIDTH_COLS,STANDARD_HEIGHT_ROWS));
        return new_img;
    }

    /**
     * 展示直方图
     * @param frame
     */
    public static void showHistogram(Mat frame) {
        List<Mat> images = new ArrayList<Mat>();
        Core.split(frame, images);
        MatOfInt histSize = new MatOfInt(256);
        Mat hist_b = images.get(0);
        Mat hist_g = images.get(1);
        Mat hist_r = images.get(2);
        int hist_w = 400; // width of the histogram image
        int hist_h = 400; // height of the histogram image
        int bin_w = (int) Math.round(hist_w / histSize.get(0, 0)[0]);
        Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0, 0, 0));
        for (int i = 1; i < histSize.get(0, 0)[0]; i++) {
            // B component or gray image
            Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_b.get(0, i-1)[0])),
                    new Point(bin_w * (i), hist_h - Math.round(hist_b.get(0, i)[0])), new Scalar(255, 0, 0), 2, 8, 0);
            // G and R components (if the image is not in gray scale)
            Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_g.get(0, i-1)[0])),
                    new Point(bin_w * (i), hist_h - Math.round(hist_g.get(0, i)[0])), new Scalar(0, 255, 0), 2, 8, 0);
            Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_r.get(0, i-1)[0])),
                    new Point(bin_w * (i), hist_h - Math.round(hist_r.get(0, i)[0])), new Scalar(0, 0, 255), 2, 8, 0);
        }
        HighGui.imshow("直方图",histImage);
        HighGui.waitKey(10);

    }
    /**
     * 展示GrayMatOfRGBList直方图
     * @param frame
     */
    public static void showGrayMatOfRGBListHistogram(Mat frame) {
        MatOfInt histSize = new MatOfInt(256);
        int hist_w = 400; // width of the histogram image
        int hist_h = 400; // height of the histogram image
        int bin_w = (int) Math.round(hist_w / histSize.get(0, 0)[0]);
        Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0, 0, 0));
        for (int i = 1; i < histSize.get(0, 0)[0]; i++) {
            // gray image
            Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(frame.get(0, i-1)[0])),
                    new Point(bin_w * (i), hist_h - Math.round(frame.get(0, i)[0])), new Scalar(255, 255, 255), 2, 8, 0);

        }
        HighGui.imshow("灰度直方图",histImage);
        HighGui.waitKey(10);

    }

}
