package cn.lyd.spszfx.mapper;

import cn.lyd.spszfx.pojo.Project;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ProjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);
}