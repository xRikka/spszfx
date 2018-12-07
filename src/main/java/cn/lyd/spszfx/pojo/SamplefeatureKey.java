package cn.lyd.spszfx.pojo;

import org.springframework.stereotype.Component;

@Component
public class SamplefeatureKey {
    private Long sample;

    private Long project;

    private Long featureextramethod;

    public Long getSample() {
        return sample;
    }

    public void setSample(Long sample) {
        this.sample = sample;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public Long getFeatureextramethod() {
        return featureextramethod;
    }

    public void setFeatureextramethod(Long featureextramethod) {
        this.featureextramethod = featureextramethod;
    }
}