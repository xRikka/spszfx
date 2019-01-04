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
     * 图像白平衡处理--灰度世界算法
     * @param src
     * @return
     */
    public static Mat whiteBalance_grayWorld(Mat src){
        List<Mat> mv = new ArrayList<>();
        Mat dst = new Mat();
        Core.split(src,mv);
        Mat b_channel = mv.get(0);
        Mat g_channel = mv.get(1);
        Mat r_channel = mv.get(2);
        double b_channel_avg = Core.mean(b_channel).val[0];
        double g_channel_avg = Core.mean(g_channel).val[0];
        double r_channel_avg = Core.mean(r_channel).val[0];
        double k = (b_channel_avg+g_channel_avg+r_channel_avg) / 3;
        double kb = k/b_channel_avg;
        double kg = k/g_channel_avg;
        double kr = k/r_channel_avg;
        Core.addWeighted(b_channel,kg,b_channel,0,0,b_channel);
        Core.merge(mv,dst);
        IOUtil.writeImg("D:\\IDEAWorkspace\\spszfx\\src\\main\\resources\\static\\images\\whiteBalance\\01.jpg",dst);
        return dst;
    }

    /**
     * 1、求取源图I的平均灰度，并记录rows和cols；
     *
     * 2、按照一定大小，分为N*M个方块，求出每块的平均值，得到子块的亮度矩阵D；
     *
     * 3、用矩阵D的每个元素减去源图的平均灰度，得到子块的亮度差值矩阵E；
     *
     * 4、用双立方差值法，将矩阵E差值成与源图一样大小的亮度分布矩阵R；
     *
     * 5、得到矫正后的图像result=I-R；
     *
     * 其中blockSize建议取值32
     * @param src
     * @param blockSize
     * @return
     */
    public static Mat lightingCompensation(Mat src,int blockSize){

        if (src.channels() == 3)
            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        double average = Core.mean(src).val[0];//源图像的亮度（灰度）均值
        int rows_new = (int)Math.ceil((double)src.rows() / blockSize);
        int cols_new = (int)Math.ceil((double)src.cols() / blockSize);
        Mat blockImage;
        blockImage = Mat.zeros(rows_new, cols_new, CvType.CV_8UC1);
        for (int i = 0; i < rows_new; i++)
        {
            for (int j = 0; j < cols_new; j++)
            {
                int rowmin = i*blockSize;
                int rowmax = (i + 1)*blockSize;
                if (rowmax > src.rows()) rowmax = src.rows();
                int colmin = j*blockSize;
                int colmax = (j + 1)*blockSize;
                if (colmax > src.cols()) colmax = src.cols();
                Mat imageROI = src.submat(new Range(rowmin, rowmax), new Range(colmin, colmax));
                double temaver = Core.mean(imageROI).val[0];//每个块矩阵的（亮度）灰度均值
                blockImage.put(i,j,temaver);
            }
        }
        Core.subtract(blockImage,new Scalar(average),blockImage);//亮点矩阵减去原图平均亮度
        Mat blockImage2 = new Mat();
        Imgproc.resize(blockImage, blockImage2, src.size());//缩放至原图大小
        Mat image2 = new Mat();
        src.convertTo(image2, CvType.CV_8UC1);
        Mat dst = new Mat();
        Core.subtract(image2,blockImage2,dst);
        dst.convertTo(src, CV_8UC1);
        return dst;
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

    /**
     * RGB直方图平衡化
     * @param src
     * @return
     */
    public static Mat RGBEqualizeHist(Mat src){
        List<Mat> mv = new ArrayList<>();
        Mat dst = new Mat();
        Core.split(src,mv);
        for(Mat channel : mv){
            Imgproc.equalizeHist(channel,channel);
        }
        Core.merge(mv,dst);
        return dst;
    }

    /**
     * 调整均值列表 1 水平 2 背景光照趋于150
     * @param RGBList
     * @return
     */
    public static List<int[]> RGBHorizontalCorrection(List<Integer> grayList,List<int[]> RGBList){
        int backgroundColor = 150;
        double ck;
        double cb;
        //计算整体斜率

        List<Double> p2pKList = new ArrayList<>();
        for(int i = 0;i < grayList.size() - 1;i++){
            int x1 = grayList.get(i);
            int x2 = grayList.get(i+1);
            /*
            double k = 斜率(x1,x2);
            p2pKList.add(k);
             */
        }
        //获取背景RGB（将特征线区域除去）
        List<Integer> backgroundList = new ArrayList<>();
        for(int i = 0;i < p2pKList.size();i++){
            /*
            if(isBackground(p2pKList.get(i))){
                backgroundList.add(grayList.get(i*2));
            }
            */
        }
        //计算水平矫正ck
        /*
            ck = getCk(p2pKList);
        */
        //计算背景RGB均值
        /*
            cb = getCb(backgroundColor,avg(backgroundList));
        */
        //计算背景差值 = 背景RGB均值 - backgroundColor

        //调整背景光照趋于150 ：new = src - 背景差值

        //返回new
        return null;
    }

}
