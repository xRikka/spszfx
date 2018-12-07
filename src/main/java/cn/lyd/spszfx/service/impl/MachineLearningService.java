package cn.lyd.spszfx.service.impl;

import cn.lyd.spszfx.common.ITensorflowModel;
import cn.lyd.spszfx.mapper.*;
import cn.lyd.spszfx.pojo.*;
import cn.lyd.spszfx.service.IMachineLearningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MachineLearningService implements IMachineLearningService {

    @Resource
    ITensorflowModel tensorflowModel;
    @Resource
    RuleMapper ruleMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    SampleMapper sampleMapper;
    @Resource
    Sample2projectMapper sample2projectMapper;
    @Resource
    SamplefeatureMapper samplefeatureMapper;

    @Override
    public Rule trainRuleBySamplefeatureKey(SamplefeatureKey key,Sample2projectKey sample2projectKey,Rule rule) throws IOException, InterruptedException {
        //读取指定路径的tensorflow的python脚本并运行
        //根据project_id创建训练模型目录
        List<Samplefeature> samplefeatures =  samplefeatureMapper.selectByPrimaryKeySelective(key);
        if(samplefeatures != null && samplefeatures.size() > 0){
            List<int[]> list_x = new ArrayList<>();
            List<Float> list_y = new ArrayList<>();
            for(Samplefeature samplefeature : samplefeatures){
                sample2projectKey.setSample(samplefeature.getSample());
                sample2projectKey.setProject(samplefeature.getProject());
                Sample2project sample2project = sample2projectMapper.selectByPrimaryKey(sample2projectKey);
                int[] rgb = new int[3];
                rgb[0] = samplefeature.getRed();
                rgb[1] = samplefeature.getGreen();
                rgb[2] = samplefeature.getBlue();
                list_x.add(rgb);
                list_y.add(sample2project.getResult());

            }
            tensorflowModel.execute(list_x,list_y);
            float[] w = tensorflowModel.getW();
            rule.setProject(key.getProject());
            rule.setRedcoefficient(w[0]);
            rule.setGreencoefficient(w[1]);
            rule.setBluecoefficient(w[2]);
            rule.setCorrect(tensorflowModel.getB());
            return rule;
        }
        return null;
    }

    @Override
    public void saveProjectRule(Long project_id, Rule rule) {
        rule.setProject(project_id);
        ruleMapper.insertSelective(rule);
    }

    @Override
    public void updateProjectRule(Long project_id, Rule rule) {
        rule.setProject(project_id);
        ruleMapper.updateByPrimaryKeySelective(rule);
    }

    @Override
    public List<Rule> findRuleByProject(Long project_id) {
        return ruleMapper.selectByProjectID(project_id);
    }

}
