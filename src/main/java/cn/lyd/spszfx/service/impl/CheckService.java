package cn.lyd.spszfx.service.impl;

import cn.lyd.spszfx.common.ITensorflowModel;
import cn.lyd.spszfx.mapper.CheckMapper;
import cn.lyd.spszfx.mapper.RuleMapper;
import cn.lyd.spszfx.pojo.Check;
import cn.lyd.spszfx.pojo.Rule;
import cn.lyd.spszfx.service.ICheckService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class CheckService implements ICheckService {

    @Resource
    private CheckMapper checkMapper;
    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private ITensorflowModel tensorflowModel;


    @Override
    public Check checkResult(Check check) {
        Rule rule = ruleMapper.selectByPrimaryKey(check.getRule());
        int r = check.getRed();
        int g = check.getGreen();
        int b = check.getRed();
        float r_w = rule.getRedcoefficient();
        float g_w = rule.getGreencoefficient();
        float b_w = rule.getBluecoefficient();
        float bias = rule.getCorrect();
        float result = r * r_w + g * g_w + b * b_w + bias;
        check.setResult(result);
        return check;
    }

    @Override
    public Check findCheckByID(Long check_id) {
        return checkMapper.selectByPrimaryKey(check_id);
    }

    @Override
    public void saveCheck(Check check) {
        checkMapper.insertSelective(check);
    }

    @Override
    public void updateCheck(Check check) {
        checkMapper.updateByPrimaryKeySelective(check);
    }

    @Override
    public List<Check> findCheckByProject(Long project_id) {
        return checkMapper.selectByProjectID(project_id);
    }
}
