package cn.lyd.spszfx.pojo;

import org.springframework.stereotype.Component;

@Component
public class Rule {
    private Long id;

    private String name;

    private Long project;

    private Boolean type;

    private Long featureextramethon;

    private Float redcoefficient;

    private Float greencoefficient;

    private Float bluecoefficient;

    private Float correct;

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

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Long getFeatureextramethon() {
        return featureextramethon;
    }

    public void setFeatureextramethon(Long featureextramethon) {
        this.featureextramethon = featureextramethon;
    }

    public Float getRedcoefficient() {
        return redcoefficient;
    }

    public void setRedcoefficient(Float redcoefficient) {
        this.redcoefficient = redcoefficient;
    }

    public Float getGreencoefficient() {
        return greencoefficient;
    }

    public void setGreencoefficient(Float greencoefficient) {
        this.greencoefficient = greencoefficient;
    }

    public Float getBluecoefficient() {
        return bluecoefficient;
    }

    public void setBluecoefficient(Float bluecoefficient) {
        this.bluecoefficient = bluecoefficient;
    }

    public Float getCorrect() {
        return correct;
    }

    public void setCorrect(Float correct) {
        this.correct = correct;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", project=" + project +
                ", type=" + type +
                ", featureextramethon=" + featureextramethon +
                ", redcoefficient=" + redcoefficient +
                ", greencoefficient=" + greencoefficient +
                ", bluecoefficient=" + bluecoefficient +
                ", correct=" + correct +
                ", memo='" + memo + '\'' +
                '}';
    }
}