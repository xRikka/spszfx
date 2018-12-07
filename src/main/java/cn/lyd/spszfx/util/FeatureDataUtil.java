package cn.lyd.spszfx.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FeatureDataUtil {

    public static String TEST_SAMPLE_PATH = "src/main/resources/static/features/RGBTestData.txt";
    public static String SAVER_MODEL_PATH = "src/main/resources/static/out_saver_model/";
    public static String PYTHON_TENSORFLOW_PATH = "src/main/resources/static/python/";

    public static int[][] readTestData(){
        List<int[]> test_list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(TEST_SAMPLE_PATH)))){
            String str = null;
            String[] str_arr = null;
            while((str = reader.readLine()) != null){
                 str_arr = str.split(",");
                int[] int_arr = new int[str_arr.length];
                 for (int i = 0;i < int_arr.length;i++){
                     int_arr[i] = Integer.parseInt(str_arr[i]);
                 }
                 test_list.add(int_arr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listToArr2(test_list);
    }

    public static int[][] listToArr2(List<int[]> list){
        int[][] temp = new int[list.size()][];
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
