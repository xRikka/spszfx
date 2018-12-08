package cn.lyd.spszfx.controller;

import cn.lyd.spszfx.pojo.Rule;
import cn.lyd.spszfx.pojo.Sample2projectKey;
import cn.lyd.spszfx.pojo.SamplefeatureKey;
import cn.lyd.spszfx.service.IImgprocService;
import cn.lyd.spszfx.service.IMachineLearningService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/train")
public class MachineLearningController {

    @Resource
    private IMachineLearningService machineLearningService;
    @Resource
    private IImgprocService imgprocService;

    @RequestMapping(value = "/{project_id}",method = RequestMethod.PUT)
    public Rule trainRuleBySamplefeatureKey(SamplefeatureKey key, Sample2projectKey sample2projectKey, Rule rule){
        try {
            return machineLearningService.trainRuleBySamplefeatureKey(key,sample2projectKey,rule);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/{project_id}/save",method = RequestMethod.POST)
    public Rule saveProjectRule(Long project_id,Rule rule){
        machineLearningService.saveProjectRule(project_id,rule);
        return rule;
    }
    @RequestMapping(value = "/{project_id}/update",method = RequestMethod.POST)
    public Rule updateProjectRule(Long project_id,Rule rule){
        machineLearningService.updateProjectRule(project_id,rule);
        return rule;
    }
    @RequestMapping(value = "/{project_id}/get",method = RequestMethod.GET)
    public List<Rule> findRuleByProject(Long project_id){
        return machineLearningService.findRuleByProject(project_id);
    }

}
