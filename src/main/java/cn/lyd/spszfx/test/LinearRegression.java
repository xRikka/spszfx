package cn.lyd.spszfx.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LinearRegression {

    private double theta0 = 0.0 ;  //截距
    private double theta1 = 0.0 ;  //斜率
    private double alpha = 0.01 ;  //学习速率

    private int max_itea = 1000 ; //最大迭代步数

    private List<Integer> datas = new ArrayList<>();

    public  LinearRegression(List<Integer> datas){
        this.datas = datas;
    }

    public  LinearRegression(List<Integer> datas,double alpha,int max_itea){
        this.datas = datas;
        this.alpha = alpha;
        this.max_itea = max_itea;
    }


    public double predict(double x){
        return theta0+theta1*x ;
    }

    public double calc_error(double x, double y) {
        return predict(x)-y;
    }



    public void gradientDescient(){
        double sum0 =0.0 ;
        double sum1 =0.0 ;

        for(int i = 0 ; i < datas.size() ;i++) {
            sum0 += calc_error(i, datas.get(i)) ;
            sum1 += calc_error(i, datas.get(i))*i;
        }

        this.theta0 = theta0 - alpha*sum0/datas.size() ;
        this.theta1 = theta1 - alpha*sum1/datas.size() ;

    }

    public void lineGre() {
        int itea = 0 ;
        while( itea< max_itea){
            //System.out.println(error_rate);
            System.out.println("The current step is :"+itea);
            System.out.println("theta0 "+theta0);
            System.out.println("theta1 "+theta1);
            System.out.println();
            gradientDescient();
            itea ++ ;
        }
    }


    /*public static void main(String[] args) throws IOException {
        LinearRegression linearRegression = new LinearRegression() ;
        linearRegression.lineGre();
        List<Double> list = new ArrayList<Double>() ;

    }*/

}
