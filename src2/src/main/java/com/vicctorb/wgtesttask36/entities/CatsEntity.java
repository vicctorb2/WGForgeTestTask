package com.vicctorb.wgtesttask36.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cats")

public class CatsEntity {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "tail_length")
    private Integer tail_length;

    @Column(name = "whiskers_length")
    private Integer whiskers_length;

    public CatsEntity() {
    }

    public CatsEntity(String name, String color, Integer tail_length, Integer whiskers_length) {
        this.color = color;
        this.name = name;
        this.tail_length = tail_length;
        this.whiskers_length = whiskers_length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getTail_length() {
        return tail_length;
    }

    public void setTail_length(Integer tail_length) {
        this.tail_length = tail_length;
    }

    public Integer getWhiskers_length() {
        return whiskers_length;
    }

    public void setWhiskers_length(Integer whiskers_length) {
        this.whiskers_length = whiskers_length;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatsEntity that = (CatsEntity) o;
        return name.equals(that.name) &&
                color.equals(that.color) &&
                tail_length.equals(that.tail_length) &&
                whiskers_length.equals(that.whiskers_length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, tail_length, whiskers_length);
    }

    @Override
    public String toString() {
        return "CatsEntity{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", tail_length=" + tail_length +
                ", whiskers_length=" + whiskers_length +
                '}';
    }
}
