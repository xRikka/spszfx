package cn.lyd.spszfx.demo01;

import org.opencv.core.*;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureExtraction {

    private Map<String,double[]> foodRGB = new HashMap<>();
    private Mat img = new Mat();
    private Mat matOfMidnormal = new Mat();
    private Mat grayMatOfMidnormal = new Mat();
    private List<Scalar> rowAvgRGB = new ArrayList<>();
    private int c_col;
    private int c_row;


    public FeatureExtraction(Mat src) {
        src.copyTo(this.img);
        init();
    }

    private void init() {

        this.c_col = this.img.cols() / 2;
        this.c_row = this.img.rows() / 2;
        initRowsAvg();
    }

    private void initRowsAvg() {
        Map<String,int[]> foodRGB = new HashMap<>();
        double[] RGBSum = new double[this.img.get(this.c_row, this.c_col).length];
        double[] RGB;
        Mat rowRange;
        this.matOfMidnormal.create(1,this.img.rows(),this.img.type());
        for (int i = 0; i < this.img.rows(); i++) {
            rowRange = img.rowRange(i, i + 1);
            Scalar mean = Core.mean(rowRange);
            this.rowAvgRGB.add(mean);
            this.matOfMidnormal.put(0,i,new double[]{mean.val[0],mean.val[1],mean.val[2]});
        }
        Imgproc.cvtColor(this.matOfMidnormal, this.grayMatOfMidnormal, Imgproc.COLOR_BGR2GRAY);
    }

    public void showHistogram(Mat frame) {
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
        HighGui.waitKey(0);

    }


    public void LocalPeakOfFeature(int minPeakDistance,double loUpDiff,double threshold) {
        List<Integer> localPeaks = new ArrayList<>();
        int slide_size = minPeakDistance;
        int start = 0;
        int over = 0;
        int max_size = this.grayMatOfMidnormal.cols();
        Mat gm = this.grayMatOfMidnormal;


        while(start < max_size){
            if(over + slide_size <= max_size){
                over += slide_size;
            }else{
                over = max_size;
            }
            Mat subGm = gm.colRange(start,over);
            MinMaxLocResult locResult = Core.minMaxLoc(subGm);
            double maxVal = locResult.maxVal;
            double minVal = locResult.minVal;
            if(locResult.maxVal > threshold + loUpDiff){
                localPeaks.add((int)locResult.maxLoc.x);
            }
            if(locResult.minVal < threshold - loUpDiff){
                localPeaks.add((int)locResult.minLoc.x);
            }

/*
            int localMaxIndex = start;
            int localMinIndex = start;
            double localMaxPeak = gm.get(0,start)[0];
            double localMinPeak = gm.get(0,start)[0];
            for(int i = start;i < over;i++){
                if(localMaxPeak < gm.get(0,i)[0]){
                    localMaxPeak = gm.get(0,i)[0];
                    localMaxIndex = i;
                }
                if(localMinPeak > gm.get(0,i)[0]){
                    localMinPeak = gm.get(0,i)[0];
                    localMinIndex = i;
                }
            }
            if(localMaxPeak > threshold + loUpDiff){
                localPeaks.add(localMaxIndex);
            }
            if(localMinPeak < threshold - loUpDiff){
                localPeaks.add(localMinIndex);
            }
*/
            start = over;
        }
        int debug = 0;
        resetFoodRGB(localPeaks);
    }

    private void resetFoodRGB(List<Integer> iList){
        foodRGB.clear();
        for(int index : iList){
            foodRGB.put(""+index,this.matOfMidnormal.get(0,index));
        }
    }



}
