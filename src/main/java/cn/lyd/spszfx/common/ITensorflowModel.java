package cn.lyd.spszfx.common;

import org.tensorflow.Tensor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ITensorflowModel {

    public Tensor run();

    public Tensor run(List<float[]> x);

    public void execute(List<int[]> x, List<Float> y) throws IOException, InterruptedException;

    public void close();

    public Tensor getInput_x();

    public void setInput_x(Tensor input_x);

    public String getModel_path();

    public void setModel_path(String path);

    public float[] getW();

    public float getB();
}
