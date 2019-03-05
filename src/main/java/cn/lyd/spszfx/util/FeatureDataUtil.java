package cn.lyd.spszfx.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureDataUtil {

    public static String TEST_SAMPLE_PATH = "src/main/resources/static/features/RGBTestData.txt";
    public static String TRAIN_SAMPLE_PATH = "src/main/resources/static/features/RGBTrainingData.txt";
    public static String SAVER_MODEL_PATH = "out_saver_model/";
    public static String PYTHON_TENSORFLOW_PATH = "src/main/resources/static/python/";

    public static float[][] readTestData(){
        List<float[]> test_list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(TEST_SAMPLE_PATH)))){
            String str = null;
            String[] str_arr = null;
            while((str = reader.readLine()) != null){
                str_arr = str.split(",");
                float[] float_arr = new float[str_arr.length];
                for (int i = 0;i < float_arr.length;i++){
                    float_arr[i] = Float.parseFloat(str_arr[i]);
                }
                test_list.add(float_arr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listToArr2OfFloat(test_list);
    }

    public static Map<String,Object> readTrainDataFeatures(){
        List<int[]> train_list = new ArrayList<>();
        List<Float> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(TRAIN_SAMPLE_PATH)))){
            String str = null;
            String[] str_arr = null;
            String feature_arr = null;
            String result = null;
            while((str = reader.readLine()) != null){
                str_arr = str.split(" ");
                feature_arr = str_arr[0];
                result = str_arr[1].substring(str_arr[1].indexOf("=") + 1);
                String[] temp = feature_arr.substring(1,feature_arr.length()-1).split(",");
                int[] int_arr = new int[temp.length];
                for (int i = 0;i < int_arr.length;i++){
                    int_arr[i] = (int)Float.parseFloat(temp[i]);
                }
                results.add(Float.parseFloat(result));
                train_list.add(int_arr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,Object> data = new HashMap<>();
        data.put("X",train_list);
        data.put("Y",results);
        return data;
    }


    public static int[][] listToArr2(List<int[]> list){
        int[][] temp = new int[list.size()][];
        for (int i = 0;i < list.size();i++)
            temp[i] = list.get(i);
        return temp;
    }

    public static float[][] listToArr2OfFloat(List<float[]> list){
        float[][] temp = new float[list.size()][];
        for (int i = 0;i < list.size();i++)
            temp[i] = list.get(i);
        return temp;
    }

    public static int[][] normalize(int[][] x_arr){
        //数据归一化
        return null;
    }

    public static int[][] standardize(int[][] x_arr){
        //数据标准化
        return null;
    }

}
