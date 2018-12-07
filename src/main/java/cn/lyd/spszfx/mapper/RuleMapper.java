package cn.lyd.spszfx.mapper;

import cn.lyd.spszfx.pojo.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Rule record);

    int insertSelective(Rule record);

    Rule selectByPrimaryKey(Long id);

    List<Rule> selectByProjectID(Long project_id);

    int updateByPrimaryKeySelective(Rule record);

    int updateByPrimaryKey(Rule record);
}