package cn.lyd.spszfx.pojo;

import org.springframework.stereotype.Component;

@Component
public class Samplefeature extends SamplefeatureKey {
    private String name;

    private Integer red;

    private Integer green;

    private Integer blue;

    private Float gray;

    private String memo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    @Override
    public String toString() {
        return "Samplefeature{" +
                "name='" + name + '\'' +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", gray=" + gray +
                ", memo='" + memo + '\'' +
                '}';
    }
}