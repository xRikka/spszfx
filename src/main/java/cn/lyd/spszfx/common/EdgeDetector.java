package cn.lyd.spszfx.common;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.List;

public abstract class EdgeDetector {

    protected List<MatOfPoint> contours = new ArrayList<>();

    public abstract boolean doDetect(Mat frame);

    public List<MatOfPoint> getContours() {
        return contours;
    }

    public void setContours(List<MatOfPoint> contours) {
        this.contours = contours;
    }
}
