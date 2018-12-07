package cn.lyd.spszfx.service;

import cn.lyd.spszfx.pojo.Check;

import java.util.List;

public interface ICheckService {

    public Check checkResult(Check check);
    public Check findCheckByID(Long check_id);
    public void saveCheck(Check check);
    public void updateCheck(Check check);
    public List<Check> findCheckByProject(Long project_id);

}
