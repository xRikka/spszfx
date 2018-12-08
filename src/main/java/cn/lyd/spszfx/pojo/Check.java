package cn.lyd.spszfx.pojo;

import org.springframework.stereotype.Component;

@Component
public class Check {
    private Long id;

    private String name;

    private Long project;

    private Long featureextramethon;

    private Long rule;

    private Integer red;

    private Integer green;

    private Integer blue;

    private Float gray;

    private Float result;

    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public Long getFeatureextramethon() {
        return featureextramethon;
    }

    public void setFeatureextramethon(Long featureextramethon) {
        this.featureextramethon = featureextramethon;
    }

    public Long getRule() {
        return rule;
    }

    public void setRule(Long rule) {
        this.rule = rule;
    }

    public Integer getRed() {
        return red;
    }

    public void setRed(Integer red) {
        this.red = red;
    }

    public Integer getGreen() {
        return green;
    }

    public void setGreen(Integer green) {
        this.green = green;
    }

    public Integer getBlue() {
        return blue;
    }

    public void setBlue(Integer blue) {
        this.blue = blue;
    }

    public Float getGray() {
        return gray;
    }

    public void setGray(Float gray) {
        this.gray = gray;
    }

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    @Override
    public String toString() {
        return "Check{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", project=" + project +
                ", featureextramethon=" + featureextramethon +
                ", rule=" + rule +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", gray=" + gray +
                ", result=" + result +
                ", memo='" + memo + '\'' +
                '}';
    }
}