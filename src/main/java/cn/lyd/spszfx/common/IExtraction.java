package cn.lyd.spszfx.common;

import cn.lyd.spszfx.model.RGBFeature;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

public interface IExtraction {

    public RGBFeature extract(Mat src, int minPeakDistance, double loUpDiff, double threshold);
    public Mat getGrayMatOfRGBList();

}
