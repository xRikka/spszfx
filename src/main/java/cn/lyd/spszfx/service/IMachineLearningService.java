package cn.lyd.spszfx.service;

import cn.lyd.spszfx.pojo.Project;
import cn.lyd.spszfx.pojo.Rule;
import cn.lyd.spszfx.pojo.Sample2projectKey;
import cn.lyd.spszfx.pojo.SamplefeatureKey;

import java.io.IOException;
import java.util.List;

public interface IMachineLearningService {

    public Rule trainRuleBySamplefeatureKey(SamplefeatureKey key, Sample2projectKey sample2projectKey, Rule rule) throws IOException, InterruptedException;
    public void saveProjectRule(Long project_id,Rule rule);
    public void updateProjectRule(Long project_id,Rule rule);
    public List<Rule> findRuleByProject(Long project_id);

}
