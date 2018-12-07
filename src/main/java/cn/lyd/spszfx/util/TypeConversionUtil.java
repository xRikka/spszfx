package cn.lyd.spszfx.util;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class TypeConversionUtil {
    /**
     * double[]转换成int[]
     * @param arr
     *           要转换的double[]
     * @return
     */
    public static int[] arrOfdouble2int(double[] arr){
        int[] ints = new int[arr.length];
        for(int i = 0;i < arr.length;i++)
            ints[i] = (int)arr[i];
        return ints;
    }
    /**
     *  根据Mat type()返回指定通道数的double[]
     * @param scalar
     * @param type
     * @return
     */
    public static double[] Scalar2doubleByType(Scalar scalar,int type){
        double[] doubles = new double[type];
        for(int i = 0;i < type;i++){
            doubles[i] = scalar.val[i];
        }
        return doubles;
    }
    /**
     * Mat转换成BufferedImage
     *
     * @param matrix
     *            要转换的Mat
     * @param fileExtension
     *            格式为 ".jpg", ".png", etc
     * @return
     */
    public static BufferedImage Mat2BufImg (Mat matrix, String fileExtension) {
        // convert the matrix into a matrix of bytes appropriate for
        // this file extension
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(fileExtension, matrix, mob);
        // convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;
    }

    /**
     * BufferedImage转换成Mat
     *
     * @param original
     *            要转换的BufferedImage
     * @param imgType
     *            bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
     * @param matType
     *            转换成mat的type 如 CvType.CV_8UC3
     */
    public static Mat BufImg2Mat (BufferedImage original, int imgType, int matType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        // Don't convert if it already has correct type
        if (original.getType() != imgType) {

            // Create a buffered image
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);

            // Draw the image onto the new buffer
            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }

        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }

    public static String listArr2String(List<int[]> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            int[] rgb = list.get(i);
            sb.append(rgb[0]+" "+rgb[1]+" "+rgb[2]).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public static String list2String(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }
}
