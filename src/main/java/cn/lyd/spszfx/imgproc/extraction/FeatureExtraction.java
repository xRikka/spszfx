package cn.lyd.spszfx.imgproc.extraction;

import cn.lyd.spszfx.common.IExtraction;
import cn.lyd.spszfx.model.RGBFeature;
import cn.lyd.spszfx.util.ImgUtil;
import cn.lyd.spszfx.util.TypeConversionUtil;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeatureExtraction implements IExtraction {

    @Resource
    private RGBFeature feature;
    private Mat grayMatOfRGBList;


    public FeatureExtraction() {
        init();
    }

    private void init() {
        this.grayMatOfRGBList = new Mat();
        this.feature = new RGBFeature();
    }

    public RGBFeature extract(Mat src,int minPeakDistance,double loUpDiff,double threshold){
        src.copyTo(feature.getSrc());
        Mat img = feature.getSrc();
        Mat rowRange;
        Mat mat_temp = new Mat();
        int type = img.channels();
        feature.setType(type);
        feature.getRGBList().clear();
        mat_temp.create(1,img.rows(),img.type());
        for (int i = 0; i < img.rows(); i++) {
            rowRange = img.rowRange(i, i + 1);
            clearEdgeBlack(rowRange);
            Scalar mean = Core.mean(rowRange);
            double[] scalar_val = BGR2RGB(mean.val);
            this.feature.getRGBList().add(TypeConversionUtil.arrOfdouble2int(scalar_val));
            mat_temp.put(0,i,TypeConversionUtil.Scalar2doubleByType(mean,type));
        }
        Imgproc.cvtColor(mat_temp, this.grayMatOfRGBList, Imgproc.COLOR_BGR2GRAY);
        localPeakOfFeature(minPeakDistance,loUpDiff,threshold);
        //showHistogram(matOfMidnormal);
        return feature;
    }

    private void clearEdgeBlack(Mat rowMat){
        double[] c_rgb = rowMat.get(0,rowMat.cols() / 2);
        for(int i = 0;i < rowMat.cols();i++){
            double[] rgb = rowMat.get(0,i);
            if(rgb[0] == 0.0 && rgb[1] == 0.0 &&rgb[2] == 0.0){
                rowMat.put(0,i,c_rgb);
            }
        }
    }

    private void printGrayMatOfRGBList(){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(new File("rowAvgGrayFeatures.txt")));
            for(int i =0;i<grayMatOfRGBList.cols();i++){
                double[] rgb = grayMatOfRGBList.get(0,i);
                StringBuilder sb = new StringBuilder();
                sb.append("["+rgb[0]+"]");
                pw.println(sb.toString());
                pw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            pw.flush();
            pw.close();
        }
    }

    private void printRGBList(){
        List<int[]> RGBList = this.feature.getRGBList();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(new File("rowAvgFeatures.txt")));
            for(int i =0;i<RGBList.size();i++){
                int[] rgb = RGBList.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("["+rgb[0]+",");
                sb.append(rgb[1]+",");
                sb.append(rgb[2]+"]");
                pw.println(sb.toString());
                pw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            pw.flush();
            pw.close();
        }
    }

    private void resetFeatures(List<Integer> iList){
        Map<String,int[]> features = this.feature.getFeatures();
        Map<String,Float> grayOfFeatures = this.feature.getGrayOfFeatures();
        if(!features.isEmpty())
            features.clear();
        List<int[]> RGBList = this.feature.getRGBList();
        for(int index : iList){
            features.put(""+index,RGBList.get(index));
            grayOfFeatures.put(""+index,(float) grayMatOfRGBList.get(0,index)[0]);
        }
    }

    private void localPeakOfFeature(int minPeakDistance,double loUpDiff,double threshold) {
        List<Integer> localPeaks = new ArrayList<>();
        int slide_size = minPeakDistance;
        int start = 0;
        int over = 0;
        int max_size = this.grayMatOfRGBList.cols();
        Mat gm = this.grayMatOfRGBList;

        while(start < max_size){
            if(over + slide_size <= max_size){
                over += slide_size;
            }else{
                over = max_size;
            }
            //利用opencv自身函数，发现有误
/*
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
*/
            //自己写的关于滑动窗口来取极值的逻辑
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
            start = over;
        }
        resetFeatures(localPeaks);
        //printRowsAvgRGB();
    }

    /**
     * 将BGR顺序变换为RGB
     * @param vals
     * @return
     */
    private double[] BGR2RGB(double[] vals){
        double[] RGB = vals.clone();
        double r = RGB[2];
        RGB[2] = RGB[0];
        RGB[0] = r;
        return RGB;
    }

    public Mat getGrayMatOfRGBList() {
        return grayMatOfRGBList.empty() ? null:grayMatOfRGBList;
    }

}
