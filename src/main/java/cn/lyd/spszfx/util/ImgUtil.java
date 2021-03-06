package cn.lyd.spszfx.util;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.*;

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
        Core.addWeighted(b_channel,kb,b_channel,0,0,b_channel);
        Core.addWeighted(g_channel,kg,g_channel,0,0,g_channel);
        Core.addWeighted(r_channel,kr,r_channel,0,0,r_channel);
        Core.merge(mv,dst);
        IOUtil.writeImg("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\whiteBalance\\01.jpg",dst);
        return dst;
    }

    public static Mat whiteBalance_perfect_Reflector(Mat src){

        return null;
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
        int hist_w = 1000; // width of the histogram image
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
        int hist_w = 1000; // width of the histogram image
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
     * 对数变换
     *  对数图像增强是图像增强的一种常见方法，其公式为： S = c log(r+1)，其中c是常数（以下算法c=255/(log(256)），这样可以实现整个画面的亮度增大此时默认v=e，即 S = c ln(r+1)。
     *  对数使亮度比较低的像素转换成亮度比较高的，而亮度较高的像素则几乎没有变化，这样就使图片整体变亮。
     * @param src
     * @return
     */
    public static Mat lightingCompensation(Mat src){
        double c = 255 / Math.log(256);
        Mat dst = Mat.zeros(src.size(),src.type());
        Core.add(src,new Scalar(1,1,1),dst);
        src.convertTo(src, CvType.CV_32F);
        Core.log(src,dst);
        Core.multiply(dst,new Scalar(c,c,c),dst);
        //归一化到0~255
        Core.normalize(dst,dst,0,255,Core.NORM_MINMAX);
        //转换成8bit图像显示
        Core.convertScaleAbs(dst,dst);
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

    public static Mat brightness(Mat src){
/*
            Mat src = imread( "F:\\my\\head_src_2.jpg" );
            imshow( "原始图片" ,src);
            Mat dst=src.clone();
            ////////////////////////////////////////////////////////////////////////// Face detection in color images
            //////////////////////////////////////////////////////////////////////////根据高光区域直方图计算进行光线补偿
                 const float thresholdco = 0.05;
                 const int thresholdnum = 100;

            int histogram[256] = {0};
            for(int i=0;i<dst.rows;i++)
            {
                for(int j=0;j<dst.cols;j++)
                {
                    int b = dst.at<Vec3b>(i,j)[0];
                    int g = dst.at<Vec3b>(i,j)[1];
                    int r = dst.at<Vec3b>(i,j)[2];
                    //计算灰度值
                    int gray = (r*299+g*587+b*114)/1000;
                    histogram[gray]++;
                }
            }

            int calnum =0;
            int total = dst.rows * dst.cols ;
            int num;
            //下面的循环得到满足系数thresholdco的临界灰度级
            for(int i =0;i<256;i++)
            {
                if((float )calnum/total < thresholdco) //得到前5%的高亮像素。
                {
                    calnum+= histogram[255-i];//histogram保存的是某一灰度值的像素个数,calnum是边界灰度之上的像素数
                    num = i;
                }
                else
                    break;
            }
            int averagegray = 0;
            calnum =0;
            //得到满足条件的象素总的灰度值
            for(int i = 255;i>=255-num;i--)
            {
                averagegray += histogram[i]*i; //总的像素的个数*灰度值
                calnum += histogram[i]; //总的像素数
            }
            averagegray /=calnum;
            //得到光线补偿的系数
            float co = 255.0/(float )averagegray;

            for(int i=0;i<dst.rows;i++)
            {
                for(int j=0;j<dst.cols;j++)
                {
                    dst.at<Vec3b>(i,j)[0]= CLAMP0255(co*dst.at<Vec3b>(i,j)[0]+0.5);
                    dst.at<Vec3b>(i,j)[1]=CLAMP0255(co*dst.at<Vec3b>(i,j)[1]+0.5);
                    dst.at<Vec3b>(i,j)[2]=CLAMP0255(co*dst.at<Vec3b>(i,j)[2]+0.5);

                }
            }
            imshow( "Face detection in color images" ,dst);




            cv::waitKey();
            return 0;

*/
        return null;
    }

    //调整整体亮度趋于指定数值
    public static Mat  averageBrightness(Mat src){
        Mat dst = new Mat();
        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGR2GRAY);
        Scalar light_avg = Core.mean(dst);
        //平均亮度值与指定值的差值
        int r = (int)(150 - light_avg.val[0]);
        //设置亮度调整的beta值
        Scalar light_r = new Scalar(r,r,r);
        Core.add(src,light_r,dst);
        return dst;
    }

    /**
     * 背景归一化处理
     * 经过平均亮度调整及处理光照不均后，若像素点的三通道（RGB）都在145-155之间，则判定此像素点为背景像素点
     * @param src
     * @return
     */
    public static Mat normalizeBackground(Mat src){
        Mat dst = new Mat();
        src.copyTo(dst);
        System.out.println();
        for(int row = 0;row < dst.rows();row++){
            Mat rowRange = src.rowRange(row, row + 1);
            double[] mean = Core.mean(rowRange).val;
            System.out.print((int)mean[0]+";;"+(int)mean[1]+";;"+(int)mean[2] + "@@");
            double maxChannel = Math.max(Math.max(mean[0],mean[1]),mean[2]);
            double minChannel = Math.min(Math.min(mean[0],mean[1]),mean[2]);
            if( (maxChannel-minChannel) < 10 ){
                for(int col = 0;col < dst.cols();col++){
                    dst.put(row,col,0,0,0);
                }
            }
//            if(mean[0] >=145 && mean[0] <= 155 && mean[1] >=145 && mean[1] <= 155 && mean[2] >=145 && mean[2] <= 155){
//                for(int col = 0;col < dst.cols();col++){
//                    dst.put(row,col,0,0,0);
//                }
//            }
        }
        System.out.println();
        imwrite("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\normalizeBackground_"+System.currentTimeMillis()+".jpg",dst);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(9,9));
        Imgproc.morphologyEx(dst,dst,Imgproc.MORPH_OPEN,kernel);
        //待改，有问题
        return dst;
    }

    public static Mat kameans_getLinesMask(Mat src,int clusterCount,int attempts){
        Mat tmp = new Mat();
        src.copyTo(tmp);
        int rows = tmp.rows();
        int cols = tmp.cols();
        Scalar[] colorLabels = new Scalar[clusterCount];
//        for(int i = 0;i < clusterCount;i++){
//            colorLabels[i] = new Scalar(i+255);
//        }
        colorLabels[0] = new Scalar(0,0,0);
        colorLabels[1] = new Scalar(255,255,255);
        Mat data = new Mat(src.rows() * src.cols(),3,CvType.CV_32FC1);
        Mat bestLabels = new Mat();
        for(int i = 0;i < rows;i++){
            for(int j = 0;j < cols;j++){
                double[] sampleValue  = tmp.get(i,j);
//                Mat sample = new Mat(1,3,data.type());
//                sample.put(0,0,sampleValue[0]);
//                sample.put(0,1,sampleValue[1]);
//                sample.put(0,2,sampleValue[2]);
//                data.push_back(sample);
                data.put(i * cols + j,0,sampleValue[0]);
                data.put(i * cols + j,1,sampleValue[1]);
                data.put(i * cols + j,2,sampleValue[2]);
                //System.out.println((i * cols + j)+" data:"+data.get(i * cols + j,0)[0] + ";;sample:" + sample.get(0,0)[0]);
            }
        }
        System.out.println("开始聚类！");
//        Mat centers = new Mat(clusterCount,3,data.type());
//        centers.put(0,0,150);
//        centers.put(0,1,150);
//        centers.put(0,2,150);
//        centers.put(1,0,0);
//        centers.put(1,1,0);
//        centers.put(1,2,0);
        Core.kmeans(data,clusterCount,bestLabels,new TermCriteria(TermCriteria.EPS+TermCriteria.MAX_ITER,10,1.0)
                ,attempts,Core.KMEANS_PP_CENTERS);
        int n = 0;
        System.out.println("结束聚类，开始生成掩码矩阵！");
        for(int i = 0;i < rows;i++){
            for(int j = 0;j < cols;j++){
                int clusterIndex = (int)bestLabels.get(i * cols + j,0)[0];
                double[] colorLabel = colorLabels[clusterIndex].val;
                tmp.put(i,j,colorLabel[0],colorLabel[1],colorLabel[2]);
            }
        }
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(9,9));
        Imgproc.morphologyEx(tmp,tmp,Imgproc.MORPH_OPEN,kernel);
        System.out.println("生成掩码矩阵结束！");
        return tmp;
    }

    public static void kameans_extractFeactures(Mat src,int clusterCount,int attempts){
        Mat tmp = new Mat();
        src.copyTo(tmp);
        int rows = tmp.rows();
        int cols = tmp.cols();
        Mat data = new Mat(src.rows() * src.cols(),3,CvType.CV_32FC1);
        Mat bestLabels = new Mat();
        for(int i = 0;i < rows;i++){//创建K-Means数据集
            for(int j = 0;j < cols;j++){
                double[] sampleValue  = tmp.get(i,j);
                data.put(i * cols + j,0,sampleValue[0]);
                data.put(i * cols + j,1,sampleValue[1]);
                data.put(i * cols + j,2,sampleValue[2]);
                //System.out.println((i * cols + j)+" data:"+data.get(i * cols + j,0)[0] + ";;sample:" + sample.get(0,0)[0]);
            }
        }
        System.out.println("开始聚类！");
        Mat centers = new Mat();
        Core.kmeans(data,clusterCount,bestLabels,new TermCriteria(TermCriteria.EPS+TermCriteria.MAX_ITER,10,0.1)
                ,attempts,Core.KMEANS_PP_CENTERS,centers);
        int n = 0;
        System.out.println("结束聚类，获得聚类中心点有："+ centers.rows());
        List<double[]> centersList = new ArrayList<>();
        Mat centers_tmp = Mat.zeros(1,clusterCount,tmp.type());
        for (int i = 0;i < centers.rows();i++){
            double[] center = new double[]{centers.get(i,0)[0],centers.get(i,1)[0],centers.get(i,2)[0]};
            System.out.println(i+":("+center[0]+","+center[1]+","+center[2]+"); ");
            centersList.add(center);
            centers_tmp.put(0,i,center);
        }
        double[] centers_avg = Core.mean(centers_tmp).val;
        System.out.println("计算聚类中心均值 Core.mean()：" + "("+centers_avg[0]+","+centers_avg[1]+","+centers_avg[2]+")");
//        double[] centers_avg2 = new double[3];
//        for(double[] c : centersList){
//            centers_avg2[0] = centers_avg2[0] + c[0];
//            centers_avg2[1] = centers_avg2[1] + c[1];
//            centers_avg2[2] = centers_avg2[2] + c[2];
//        }
//        centers_avg2[0] = centers_avg2[0] / clusterCount;
//        centers_avg2[1] = centers_avg2[1] / clusterCount;
//        centers_avg2[2] = centers_avg2[2] / clusterCount;
//        System.out.println("计算聚类中心均值 List：" + "("+centers_avg2[0]+","+centers_avg2[1]+","+centers_avg2[2]+")");
    }

    /**
     * 处理图片光照不均问题
     * 分两个步骤：
     * 1，对较亮区域进行降光处理，较暗区域基本不变
     * 2，反转原图片，对较暗区域进行光照补偿，较亮区域基本不变
     * @param src 原图片
     * @param kernelSize 内核尺寸
     * @param anchor 内核中心点
     * @return 消除光照影响后的图片
     */
    public static Mat cleanBrightnessEffect(Mat src,Size kernelSize,Point anchor){
        Mat gray = new Mat();
        Mat invertedGray = new Mat();
        Mat dst = new Mat();
        Mat compensationMat = new Mat();
        Mat reductionMat = new Mat();
        //Mat mergedMat = new Mat();
        Mat grayBackground =  new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,kernelSize,anchor);
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);
        //对较亮区域进行降光处理，较暗区域基本不变
        Imgproc.dilate(gray,grayBackground,kernel,anchor,10);
        imwrite("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_grayBackground_0.jpg",reductionMat);
        Scalar avgBrightness = Core.mean(grayBackground);
        Core.subtract(grayBackground,avgBrightness,reductionMat);
        imwrite("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_reductionMat_0.jpg",reductionMat);
        Imgproc.cvtColor(reductionMat,reductionMat,Imgproc.COLOR_GRAY2BGR);
        Core.subtract(src,reductionMat,dst);
        //反转原图片，对较暗区域进行光照补偿，较亮区域基本不变
        Core.bitwise_not(gray,invertedGray);
        Imgproc.erode(invertedGray,grayBackground,kernel,anchor,10);
        Scalar invertedAvgBrightness = Core.mean(grayBackground);
        Core.subtract(grayBackground,invertedAvgBrightness,compensationMat);
        imwrite("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\test\\cleanBrightnessEffect_compensationMat_0.jpg",compensationMat);
        //Core.subtract(compensationMat,reductionMat,mergedMat);
        Imgproc.cvtColor(compensationMat,compensationMat,Imgproc.COLOR_GRAY2BGR);
        Core.add(dst,compensationMat,dst);
        return dst;

    }

    public static Mat SegmentByWatershed(Mat src){
        return null;
    }

    public static Map<String,Object> extractFeactures(Mat src,Mat mask,int itemCount){
        Mat dst = Mat.zeros(mask.rows(),mask.cols(),CvType.CV_8UC1);
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        //mask.convertTo(mask,CvType.CV_8UC1);
        //mask.copyTo(dst);
        for(int row = 0;row < mask.rows();row++){
            for(int col = 0;col < mask.cols();col++){
                dst.put(row,col,mask.get(row,col)[0]);
            }
        }
        Imgproc.findContours(dst,contours,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        if(contours.size() != itemCount){
            System.out.println("size:"+contours.size()+"获取轮廓数量不符！");
            return null;
        }
        List<Mat> itemList = new ArrayList<>();
        List<int[]> featureList = new ArrayList<>();
        for(int i = 0;i < contours.size();i++){
            RotatedRect rect =Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
            Point[] points = new Point[4];
            rect.points(points);
            int[] x_arr = new int[4];
            int[] y_arr = new int[4];
            for(int j = 0;j < points.length;j++){
                x_arr[j] = (int)points[j].x;
                y_arr[j] = (int)points[j].y;
            }
            //x，y坐标集分别由大到小排序
            Arrays.sort(x_arr);
            Arrays.sort(y_arr);
            int width = x_arr[x_arr.length - 1] - x_arr[0];
            int height = y_arr[y_arr.length - 1] - y_arr[0];
            int sub_height = height / 4;
            Mat item = new Mat(src,new Rect(x_arr[0],y_arr[0] + sub_height,width,height - (sub_height * 2)));
            double[] rgb = Core.mean(item).val;
            featureList.add(new int[]{(int)rgb[0],(int)rgb[1],(int)rgb[2]});
            itemList.add(item);
            ImgUtil.kameans_extractFeactures(item,10,3);
        }
        Map<String,Object> features = new HashMap<>();
        features.put("itemList",itemList);
        features.put("featureList",featureList);
        features.put("itemCount",itemCount);
        return features;
    }

}
