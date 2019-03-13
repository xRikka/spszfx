package cn.lyd.spszfx.imgproc.correction;

import cn.lyd.spszfx.common.ICorrection;
import cn.lyd.spszfx.util.ImgUtil;
import cn.lyd.spszfx.util.IOUtil;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

@Component
public class HorizontalCorrection implements ICorrection {

    private int HOUGH_THRESHOLD = 100;
    private int EDGE_THRESHOLD = 10;

    public Mat degree(Mat src){
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();
        Mat Lines = new Mat();
        Mat threshImage = new Mat();
        Mat kernelImage = new Mat();
        Mat houghLinesTemp = new Mat();
        src.copyTo(houghLinesTemp);
        Imgproc.cvtColor(src,grayImage,Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(grayImage,grayImage,9);
        Imgproc.Canny(grayImage,detectedEdges,EDGE_THRESHOLD, EDGE_THRESHOLD * 3);
        //二值化
        Imgproc.threshold(detectedEdges,threshImage,ImgUtil.BINARY_THRESHOLD, 255,Imgproc.THRESH_BINARY);
        //形态学操作
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        //Imgproc.erode(threshImage,kernelImage,kernel);//腐蚀
        Imgproc.dilate(threshImage,kernelImage,kernel);//膨胀
        //imwrite(IOUtil.IMAGE_Hough_PATH+"HOUGH_CANNY.jpg",kernelImage);
        int ht = HOUGH_THRESHOLD;
        Imgproc.HoughLines(kernelImage,Lines,1,Math.PI / 180,ht);
        while(Lines.rows() <= 0 && ht > 0){
            ht -= 25;
            Imgproc.HoughLines(kernelImage,Lines,1,Math.PI / 180,ht);
        }
        double sum = 0.0;
        for (int x = 0; x < Lines.rows(); x++)
        {
            double[] vec = Lines.get(x, 0);

            double rho = vec[0];
            double theta = vec[1];

            Point pt1 = new Point();
            Point pt2 = new Point();

            double a = Math.cos(theta);
            double b = Math.sin(theta);

            double x0 = a * rho;
            double y0 = b * rho;
            sum += theta;

            pt1.x = Math.round(x0 + 1000 * (-b));
            pt1.y = Math.round(y0 + 1000 * (a));
            pt2.x = Math.round(x0 - 1000 * (-b));
            pt2.y = Math.round(y0 - 1000 * (a));

            if (theta >= 0)
            {
                Imgproc.line(houghLinesTemp, pt1, pt2, new Scalar(255, 0, 0), 1, Imgproc.LINE_4, 0);
            }
        }
        Mat degreeImage = new Mat();
        src.copyTo(degreeImage);
        double average = sum / Lines.rows();
        double angle = average / Math.PI * 180 - 90;//计算角度
        Point centor = new Point(src.cols() / 2,src.rows() / 2);
        Mat affineTians = Imgproc.getRotationMatrix2D(centor,angle,1);
        Imgproc.warpAffine(houghLinesTemp,degreeImage,affineTians,degreeImage.size(),Imgproc.INTER_NEAREST);
        //imwrite(IOUtil.IMAGE_Hough_PATH+"HOUGHLINES_DEGREE.jpg",degreeImage);
        Imgproc.warpAffine(src,degreeImage,affineTians,degreeImage.size(),Imgproc.INTER_NEAREST);
        //imwrite(IOUtil.IMAGE_Hough_PATH+"HOUGH.jpg",houghLinesTemp);
        //imwrite(IOUtil.IMAGE_Hough_PATH+"HOUGH_DEGREE.jpg",degreeImage);
        return degreeImage;

    }

}
