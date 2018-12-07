package cn.lyd.spszfx.mapper;

import cn.lyd.spszfx.pojo.Check;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CheckMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Check record);

    int insertSelective(Check record);

    Check selectByPrimaryKey(Long id);

    List<Check> selectByProjectID(Long project_id);

    int updateByPrimaryKeySelective(Check record);

    int updateByPrimaryKey(Check record);
}