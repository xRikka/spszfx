package cn.lyd.spszfx.tensorflow;

import cn.lyd.spszfx.common.ITensorflowModel;
import cn.lyd.spszfx.util.FeatureDataUtil;
import cn.lyd.spszfx.util.TypeConversionUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class TensorFlowModel implements ITensorflowModel {

    protected String model_path = FeatureDataUtil.SAVER_MODEL_PATH + "model.pb";
    protected Graph graph;
    protected Tensor input_x = null;
    protected float[] w;
    protected float b;

    public TensorFlowModel(){
        init("");
    }

    public TensorFlowModel(String path){
        init(path);
    }

    protected void init(String path){
        String pathTemp = path;
        if("".equals(path)){
            pathTemp = this.model_path;
        }
        try {
            //导入图
            Graph graph = new Graph();
            byte[] graphBytes = IOUtils.toByteArray(new FileInputStream(pathTemp));
            graph.importGraphDef(graphBytes);
            initGraph_variables(graph);
            this.graph = graph;
            this.model_path = pathTemp;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGraph_variables(Graph g){
        try(Session session = new Session(g)){
            //相当于TensorFlow Python中的sess.run(z,feed_dict = {'x': 10.0})
            Tensor tensor_w = session.runner()
                    .fetch("weight").run().get(0);
            this.w = (float[]) tensor_w.copyTo(new float[(int)tensor_w.shape()[0]]);
            this.b = session.runner().fetch("bias").run().get(0).floatValue();
        }
    }
    @Override
    public Tensor run(List<int[]> x){
        int[][] x_arr = FeatureDataUtil.listToArr2(x);
        //根据图建立Session
        Tensor result = null;
        try(Session session = new Session(this.graph)){
            //相当于TensorFlow Python中的sess.run(z,feed_dict = {'x': 10.0})
            result = session.runner()
                    .feed("X", Tensor.create(x_arr))
                    .fetch("pred").run().get(0);
        }
        return result;
    }
    @Override
    public Tensor run(){
        if(this.input_x != null){
            //根据图建立Session
            Tensor result = null;
            try(Session session = new Session(this.graph)){
                //相当于TensorFlow Python中的sess.run(z,feed_dict = {'x': 10.0})
                result = session.runner()
                        .feed("X", this.input_x)
                        .fetch("pred").run().get(0);
            }
            return result;
        }else{
            return null;
        }
    }
    @Override
    public void execute(List<int[]> x, List<Float> y) throws IOException, InterruptedException {
        String[] args = {"python",FeatureDataUtil.PYTHON_TENSORFLOW_PATH+"Main.py",
                TypeConversionUtil.listArr2String(x,'\n'),TypeConversionUtil.list2String(y,'\n')};
        Process pr = Runtime.getRuntime().exec(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println("python line:" + line);
        }
        in.close();
        pr.waitFor();
        init(model_path);
    }
    @Override
    public void close(){
        this.graph.close();
    }
    @Override
    public Tensor getInput_x() {
        return input_x;
    }
    @Override
    public void setInput_x(Tensor input_x) {
        this.input_x = input_x;
    }
    @Override
    public String getModel_path(){ return this.model_path; }
    @Override
    public void setModel_path(String path){ init(path); }
    @Override
    public float[] getW() { return w; }
    @Override
    public float getB() { return b; }


}