package cn.lyd.spszfx.service;

import cn.lyd.spszfx.pojo.Check;

import java.awt.image.BufferedImage;
import java.util.List;

public interface IImgprocService {

    public List<Check> extractFeatures(BufferedImage buffImg,Long project_id,int minPeakDistance,double loUpDiff,double threshold);
    public List<Check> extractFeatures(String imgPath,Long project_id,int minPeakDistance,double loUpDiff,double threshold);
    public List<Check> extractFeaturesFromSamples(String samples_path,Long project_id,int minPeakDistance,double loUpDiff,double threshold);



}
