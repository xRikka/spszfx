package cn.lyd.spszfx.service.impl;

import cn.lyd.spszfx.common.ICorrection;
import cn.lyd.spszfx.common.IExtraction;
import cn.lyd.spszfx.common.RegionalDetection;
import cn.lyd.spszfx.imgproc.extraction.FeatureExtraction;
import cn.lyd.spszfx.model.RGBFeature;
import cn.lyd.spszfx.pojo.Check;
import cn.lyd.spszfx.service.IImgprocService;
import cn.lyd.spszfx.util.IOUtil;
import cn.lyd.spszfx.util.TypeConversionUtil;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ImgprocService implements IImgprocService {

    @Resource
    private RegionalDetection rectRegionalDetection;//后期可能需要动态注入实现类
    @Resource
    private ICorrection horizontalCorrection;//后期可能需要动态注入实现类
    @Resource
    private FeatureExtraction featureExtraction;//后期可能需要动态注入实现类

    @Override
    public List<Check> extractFeatures(Mat frame,Long project_id,int minPeakDistance,double loUpDiff,double threshold) {
        return toDo(frame,project_id,minPeakDistance,loUpDiff,threshold);
    }


    @Override
    public List<Check> extractFeaturesFromSamples(String samples_path,Long project_id,int minPeakDistance,double loUpDiff,double threshold) {
        List<Mat> samples = IOUtil.readBatchImg(samples_path);
        List<Check> checks = new ArrayList<>();
        List<Check> checksOfOne = null;
        for(Mat frame : samples){
            if ((checksOfOne = toDo(frame,project_id,minPeakDistance,loUpDiff,threshold)) != null)
                checks.addAll(checksOfOne);
        }
        return checks.size() > 0 ? checks:null;
    }

    private List<Check> toDo(Mat frame,Long project_id,int minPeakDistance,double loUpDiff,double threshold){
        Mat roi = rectRegionalDetection.toDo(frame);
        //Mat corrected_roi = horizontalCorrection.degree(roi);
        RGBFeature feature = featureExtraction.extract(roi,minPeakDistance,loUpDiff,threshold);
        Map<String,int[]> features = feature.getFeatures();
        Map<String,Float> grayOfFeatures = feature.getGrayOfFeatures();
        if(!features.isEmpty()){
            List<Check> checks = new ArrayList<>();
            Iterator<String> it = features.keySet().iterator();
            while(it.hasNext()){
                String index = it.next();
                int[] temp = features.get(index);
                Check check = new Check();
                check.setRed(temp[0]);
                check.setGreen(temp[1]);
                check.setBlue(temp[2]);
                check.setGray(grayOfFeatures.get(index));
                check.setProject(project_id);
                checks.add(check);
            }
            return checks;
        }
        return null;

    }




}
