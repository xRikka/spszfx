package cn.lyd.spszfx.controller;

import cn.lyd.spszfx.pojo.Check;
import cn.lyd.spszfx.service.ICheckService;
import cn.lyd.spszfx.service.IImgprocService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/check")
public class CheckController {

    @Resource
    private ICheckService checkService;
    @Resource
    private IImgprocService imgprocService;

    @RequestMapping(value = "/{check_id}/result/save",method = RequestMethod.PUT)
    public Check saveCheckResult(Long check_id){
        Check check = checkService.findCheckByID(check_id);
        check = checkService.checkResult(check);
        checkService.updateCheck(check);
        return checkService.findCheckByID(check_id);
    }

    @RequestMapping(value = "/{check_id}/result/get",method = RequestMethod.GET)
    public Check getCheckResult(Long check_id){
        return checkService.findCheckByID(check_id);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Check saveCheck(Check check){
        checkService.saveCheck(check);
        return checkService.findCheckByID(check.getId());
    }


}
