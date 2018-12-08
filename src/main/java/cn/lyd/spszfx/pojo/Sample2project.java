package cn.lyd.spszfx.pojo;

import org.springframework.stereotype.Component;

@Component
public class Sample2project extends Sample2projectKey {
    private Float result;

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Sample2project{" +
                "result=" + result +
                '}';
    }
}