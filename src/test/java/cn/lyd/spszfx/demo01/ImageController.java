package cn.lyd.spszfx.demo01;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageController {

    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    private static String TEST_IMAGE_PATH_FROM_SPRING = "/images/IMG_20181024_155534.jpg";
    public static String TEST_POSS_IMAGE_PREFIX = "IMG_RE_01";
    public static String TEST_RES_IMAGE_PATHNAME = "IMG_RE_01.jpg";
    public static String TEST_RES_IMAGE_PATHNAME_ROTATED = "IMG_RE_01_rotated.jpg";
    public static String TEST_IMAGE_PATH_FROM_TEST = "E:\\IdeaProjects\\spszfx\\src\\test\\resources\\images\\IMG_20181024_155534.jpg";
    public static String TEST_IMAGE_PATH_FROM_TEST_ROTATED = "E:\\IdeaProjects\\spszfx\\src\\test\\resources\\images\\IMG_20181024_155534_01.jpg";
    public boolean CreateFile(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
                return true;
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }


    /**
     * 读取一张图片的RGB值
     *
     * @throws Exception
     */
    public void getImagePixel(String image) throws Exception {
        int[] rgb = new int[3];
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("读取文件失败！");
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        System.out.println("width=" + width + ",height=" + height + ".");
        System.out.println("minx=" + minx + ",miniy=" + miny + ".");
        //StringBuilder sb = new StringBuilder();

        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                //人眼关注度
                //int gray = (int) (0.3 * rgb[0] + 0.59 * rgb[1] + 0.11 * rgb[2]);
                //平均值
                int gray = (rgb[0]+rgb[1]+rgb[2])/3;
                if(gray > 120)//阈值=100
                    gray = 255;
                else gray = 0;
                int newPixel = colorToRGB(255, gray, gray, gray);
                bi.setRGB(i, j, newPixel);

            }
        }
        //获取准确位置区域
        //BufferedImage subi = bi.getSubimage((int)(minx+width*0.45),(int)(miny+height*0.3),(int)(width*0.05),(int)(height*0.4));
        File f = new File("IMG_RE_01.jpg");
        CreateFile(f);
        ImageIO.write(bi, "JPEG", f);
    }

    /**
     * 返回屏幕色彩值
     *
     * @param x
     * @param y
     * @return
     * @throws AWTException
     */
    public int getScreenPixel(int x, int y) throws AWTException { // 函数返回值为颜色的RGB值。
        Robot rb = null; // java.awt.image包中的类，可以用来抓取屏幕，即截屏。
        rb = new Robot();
        Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
        Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
        System.out.println(di.width);
        System.out.println(di.height);
        Rectangle rec = new Rectangle(0, 0, di.width, di.height);
        BufferedImage bi = rb.createScreenCapture(rec);
        int pixelColor = bi.getRGB(x, y);

        return 16777216 + pixelColor; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。
    }

    /**
     * 颜色分量转换为RGB值
     *
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        int x = 0;
        ImageController rc = new ImageController();
        //x = rc.getScreenPixel(100, 345);

        System.out.println(x + " - ");
        rc.getImagePixel(TEST_IMAGE_PATH_FROM_TEST);
    }


}
