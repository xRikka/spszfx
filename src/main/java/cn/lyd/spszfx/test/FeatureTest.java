package cn.lyd.spszfx.test;


import cn.lyd.spszfx.mapper.Sample2projectMapper;
import cn.lyd.spszfx.mapper.SampleMapper;
import cn.lyd.spszfx.mapper.SamplefeatureMapper;
import cn.lyd.spszfx.pojo.*;
import cn.lyd.spszfx.service.ICheckService;
import cn.lyd.spszfx.service.IImgprocService;
import cn.lyd.spszfx.service.IMachineLearningService;
import cn.lyd.spszfx.util.FeatureDataUtil;
import cn.lyd.spszfx.tensorflow.TensorFlowModel;
import cn.lyd.spszfx.util.IOUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tensorflow.Tensor;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeatureTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //File f = new File("static/images/subimages/IMG_roi_0.jpg");
        /*Mat src = IOUtil.readImg("/IDEAWorkspace/spszfx/src/main/resources/static/images/subimages/IMG_roi_0.jpg");
        Mat gray = new Mat();
        Mat det = new Mat();
        int threshold = 10;
        System.out.println(src.cols());*/
       /* Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray,det,5);
        Imgproc.Canny(det,det,threshold,threshold * 3);*/
        //Imgproc.Sobel(src,det,src.depth(),1,0);
        //imwrite("E:\\IdeaProjects\\spszfx\\src\\main\\resources\\static\\images\\subimages\\IMG_roi_1.jpg",det);



        /*
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
        Map<String,Object> map = FeatureDataUtil.readTrainDataFeatures();
        List<int[]> list_x = (ArrayList<int[]>)map.get("X");
        List<Float> list_y = (ArrayList<Float>)map.get("Y");
        TensorFlowModel tfModel = new TensorFlowModel(FeatureDataUtil.SAVER_MODEL_PATH+"model.pb");
        tfModel.execute(list_x, list_y);
        tfModel.setInput_x(Tensor.create(FeatureDataUtil.readTestData()));
        Tensor preds = tfModel.run();
        float[][] preds_arr = (float[][]) preds.copyTo(new float[(int)preds.shape()[0]][1]);
        System.out.println("result :");
        for (float[] pred : preds_arr){
            System.out.println(pred[0]);
        }


    }

    @Resource
    private ICheckService checkService;
    @Resource
    private IMachineLearningService machineLearningService;
    @Resource
    private IImgprocService imgprocService;

    SamplefeatureKey key = new SamplefeatureKey();
    Sample2projectKey sample2projectKey = new Sample2projectKey();
    Long project_id = 1L;

    @Test
    public void CheckControllerTest(){
        Long check_id = 1L;
        //List<Check> checks = checkService.findCheckByProject(1L);
        Check check = new Check();
        check.setRed(102);
        check.setGreen(83);
        check.setBlue(99);
        check.setProject(1L);
        check.setRule(1L);
        check = checkService.checkResult(check);
        //checkService.updateCheck(check);
        System.out.println(check);
    }
    @Test
    public void ImgprocControllerTest(){
        Mat frame = IOUtil.readImg(IOUtil.TEST_IMAGE_PATH_FROM_TEST);
        List<Check> checks =  imgprocService.extractFeatures(frame,project_id,200,30,150);
        for(Check check : checks)
            System.out.println(check);
    }

    @Resource
    SamplefeatureMapper samplefeatureMapper;
    @Resource
    Sample2projectMapper sample2projectMapper;
    @Resource
    SampleMapper sampleMapper;

    @Test
    public void saveSamplefeatures(){
        Map<String,Object> map = FeatureDataUtil.readTrainDataFeatures();
        List<int[]> list_x = (ArrayList<int[]>)map.get("X");
        List<Float> list_y = (ArrayList<Float>)map.get("Y");
        Samplefeature sf = new Samplefeature();
        Sample s = new Sample();
        Sample2project s2p = new Sample2project();
        sf.setFeatureextramethod(1L);
        sf.setProject(1L);
        s2p.setProject(1L);
        for(int i = 0;i < list_x.size();i++){
            sf.setRed(list_x.get(i)[0]);
            sf.setGreen(list_x.get(i)[1]);
            sf.setBlue(list_x.get(i)[2]);
            sf.setSample((long) i);

            s.setId((long)i);

            s2p.setSample((long)i);
            s2p.setResult(list_y.get(i));
            sampleMapper.insertSelective(s);
            sample2projectMapper.insertSelective(s2p);
            samplefeatureMapper.insertSelective(sf);
        }

    }

    @Test
    public void MachineLearningControllerTest(){
        try {
            Rule rule = machineLearningService.findRuleByKey(1L);
            Rule rule_result = machineLearningService.trainRuleBySamplefeatureKey(key,sample2projectKey,rule);
            machineLearningService.updateProjectRule(project_id,rule);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){
        File dir = new File("src/main/resources/static/images/temp");
        File[] filesList = dir.listFiles();
        Mat mat = null;
        for(int i = 0;i< 100;i++){
            for(File f : filesList){
                mat = imread(f.getPath());
                imwrite(f.getPath(),mat);
                mat.release();
            }
            System.out.println(i);
        }
    }
}
