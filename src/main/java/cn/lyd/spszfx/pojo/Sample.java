package cn.lyd.spszfx.pojo;

import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class Sample {
    private Long id;

    private String name;

    private Date time;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}