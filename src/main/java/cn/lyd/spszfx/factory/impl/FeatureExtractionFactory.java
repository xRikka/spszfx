package cn.lyd.spszfx.factory.impl;

import cn.lyd.spszfx.common.IExtraction;
import cn.lyd.spszfx.factory.IExtractionFactory;
import cn.lyd.spszfx.imgproc.extraction.FeatureExtraction;

public class FeatureExtractionFactory implements IExtractionFactory {

    private IExtraction extraction = null;

    @Override
    public IExtraction createExtraction(String name) {
        if(extraction == null){
            extraction = new FeatureExtraction();
        }
        return extraction;
    }
}
