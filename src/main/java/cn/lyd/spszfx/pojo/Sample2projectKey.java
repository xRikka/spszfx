package cn.lyd.spszfx.pojo;

import org.springframework.stereotype.Component;

@Component
public class Sample2projectKey {
    private Long sample;

    private Long project;

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

    @Override
    public String toString() {
        return "Sample2projectKey{" +
                "sample=" + sample +
                ", project=" + project +
                '}';
    }
}