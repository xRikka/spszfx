package cn.lyd.spszfx.factory;

import cn.lyd.spszfx.common.IExtraction;

public interface IExtractionFactory {

    public IExtraction createExtraction(String name);

}
