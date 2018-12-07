package cn.lyd.spszfx.test;


import cn.lyd.spszfx.util.FeatureDataUtil;
import cn.lyd.spszfx.tensorflow.TensorFlowModel;
import org.tensorflow.Tensor;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class FeatureTest {

    public static void main(String[] args){
        /*System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        RectRegionDetection rd = new RectRegionDetection(new CannyEdgeDetector());
        File dir = new File("src/main/resources/static/images/src");
        File[] filesList = dir.listFiles();
        for(File f : filesList){
            rd.toDo(imread(f.getPath()));
        }
        rd.writeToLocal();
        HorizontalCorrection.HoughLinesDegree(rd.getRoi_images().get(0),new Mat());
        Mat img = imread("src/main/resources/static/images/hough/HOUGH_DEGREE.jpg");
        FeatureExtraction fe = new FeatureExtraction(img);
        fe.LocalPeakOfFeature(200,30,150);
        Map map = fe.getFoodRGB();*/
        TensorFlowModel tfModel = new TensorFlowModel(FeatureDataUtil.SAVER_MODEL_PATH+"model.pb");
        tfModel.setInput_x(Tensor.create(FeatureDataUtil.readTestData()));
        Tensor preds = tfModel.run();
        float[][] preds_arr = (float[][]) preds.copyTo(new float[(int)preds.shape()[0]][1]);
        System.out.println("result :");
        for (float[] pred : preds_arr){
            System.out.println(pred[0]);
        }

    }
}
