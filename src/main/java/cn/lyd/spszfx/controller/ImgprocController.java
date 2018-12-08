package cn.lyd.spszfx.controller;

import cn.lyd.spszfx.pojo.Check;
import cn.lyd.spszfx.service.IImgprocService;
import cn.lyd.spszfx.service.IMachineLearningService;
import cn.lyd.spszfx.util.IOUtil;
import cn.lyd.spszfx.util.TypeConversionUtil;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.List;

@RestController
@RequestMapping("/imgproc")
public class ImgprocController {

    @Resource
    private IImgprocService imgprocService;
    @Resource
    private IMachineLearningService machineLearningService;

    @RequestMapping(value = "/extract/{project_id}/image",method = RequestMethod.POST)
    public List<Check> extractFeatures(BufferedImage buffImg,Long project_id,int minPeakDistance,double loUpDiff,double threshold){
        Mat frame = TypeConversionUtil.BufImg2Mat(buffImg,buffImg.getType(),CvType.CV_8UC3);
        return imgprocService.extractFeatures(frame,project_id,minPeakDistance,loUpDiff,threshold);
    }

    @RequestMapping(value = "/extract/{project_id}/path",method = RequestMethod.POST)
    public List<Check> extractFeatures(String imgPath,Long project_id,int minPeakDistance,double loUpDiff,double threshold){
        Mat frame = IOUtil.readImg(imgPath);
        return imgprocService.extractFeatures(frame,project_id,minPeakDistance,loUpDiff,threshold);
    }

    @RequestMapping(value = "/extract/{project_id}/samples",method = RequestMethod.POST)
    public List<Check> extractFeaturesFromSamples(String samples_path, Long project_id, int minPeakDistance, double loUpDiff, double threshold){
        return imgprocService.extractFeaturesFromSamples(samples_path,project_id,minPeakDistance,loUpDiff,threshold);
    }


}
