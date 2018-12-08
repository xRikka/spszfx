package cn.lyd.spszfx.service;

import cn.lyd.spszfx.pojo.Check;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.util.List;

public interface IImgprocService {

    public List<Check> extractFeatures(Mat frame, Long project_id, int minPeakDistance, double loUpDiff, double threshold);
    public List<Check> extractFeaturesFromSamples(String samples_path,Long project_id,int minPeakDistance,double loUpDiff,double threshold);



}
