package cn.lyd.spszfx.common;

import org.opencv.core.Mat;

public interface IDetection {

    public abstract Mat toDo(Mat frame);

}
