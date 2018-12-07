package cn.lyd.spszfx.mapper;

import cn.lyd.spszfx.pojo.Sample2project;
import cn.lyd.spszfx.pojo.Sample2projectKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface Sample2projectMapper {
    int deleteByPrimaryKey(Sample2projectKey key);

    int insert(Sample2project record);

    int insertSelective(Sample2project record);

    Sample2project selectByPrimaryKey(Sample2projectKey key);

    int updateByPrimaryKeySelective(Sample2project record);

    int updateByPrimaryKey(Sample2project record);
}