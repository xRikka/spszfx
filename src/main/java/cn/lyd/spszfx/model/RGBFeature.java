package cn.lyd.spszfx.model;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RGBFeature {
    private Map<String,int[]> features;
    private Map<String,Float> grayOfFeatures;
    private Mat src;
    private List<int[]> RGBList;
    private int type;

    public Map<String, int[]> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, int[]> features) {
        this.features = features;
    }

    public Map<String, Float> getGrayOfFeatures() {
        return grayOfFeatures;
    }

    public void setGrayOfFeatures(Map<String, Float> grayOfFeatures) {
        this.grayOfFeatures = grayOfFeatures;
    }

    public Mat getSrc() {
        return src;
    }

    public void setSrc(Mat src) {
        this.src = src;
    }

    public List<int[]> getRGBList() {
        return RGBList;
    }

    public void setRGBList(List<int[]> RGBList) {
        this.RGBList = RGBList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
