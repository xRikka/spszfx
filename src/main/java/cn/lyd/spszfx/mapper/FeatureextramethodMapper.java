package cn.lyd.spszfx.mapper;

import cn.lyd.spszfx.pojo.Featureextramethod;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FeatureextramethodMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Featureextramethod record);

    int insertSelective(Featureextramethod record);

    Featureextramethod selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Featureextramethod record);

    int updateByPrimaryKey(Featureextramethod record);
}