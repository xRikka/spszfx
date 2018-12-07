package cn.lyd.spszfx.mapper;

import cn.lyd.spszfx.pojo.Sample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SampleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Sample record);

    int insertSelective(Sample record);

    Sample selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Sample record);

    int updateByPrimaryKey(Sample record);
}