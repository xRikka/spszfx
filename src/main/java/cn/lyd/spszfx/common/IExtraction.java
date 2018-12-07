package cn.lyd.spszfx.common;

import org.opencv.core.Mat;

public interface IExtraction {

    public void extract(Mat src, int minPeakDistance, double loUpDiff, double threshold);

}
