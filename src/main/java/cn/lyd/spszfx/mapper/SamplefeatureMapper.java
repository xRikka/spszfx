package cn.lyd.spszfx.mapper;

import cn.lyd.spszfx.pojo.Samplefeature;
import cn.lyd.spszfx.pojo.SamplefeatureKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SamplefeatureMapper {
    int deleteByPrimaryKey(SamplefeatureKey key);

    int insert(Samplefeature record);

    int insertSelective(Samplefeature record);

    Samplefeature selectByPrimaryKey(SamplefeatureKey key);

    List<Samplefeature> selectByPrimaryKeySelective(SamplefeatureKey key);

    int updateByPrimaryKeySelective(Samplefeature record);

    int updateByPrimaryKey(Samplefeature record);
}