package dev.pdsf.eaglai.model;

import com.fasterxml.jackson.annotation.JsonView;
import dev.pdsf.eaglai.model.types.*;
import dev.pdsf.eaglai.resource.Views;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "descriptions")
public class Description {
    @JsonView(Views.Internal.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @JsonView(Views.Minimal.class)
    private Age age;
    @JsonView(Views.Minimal.class)
    private Sex sex;

    @JsonView(Views.Minimal.class)
    private SkinTone skinTone;

    @JsonView(Views.Minimal.class)
    private EyeColor eyeColor;

    @JsonView(Views.Minimal.class)
    private HairType hairType;

    @JsonView(Views.Minimal.class)
    private HairColor hairColor;

    @JsonView(Views.Minimal.class)
    private boolean facialHair;

    @OneToOne(mappedBy = "description")
    private Contact contact;

    protected Description() {
    }

    public Description(Age age, Sex sex, SkinTone skinTone, EyeColor eyeColor, HairType hairType, HairColor hairColor, boolean facialHair) {
        this.age = age;
        this.sex = sex;
        this.skinTone = skinTone;
        this.eyeColor = eyeColor;
        this.hairType = hairType;
        this.hairColor = hairColor;
        this.facialHair = facialHair;
    }

    public Long getId() {
        return id;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public SkinTone getSkinTone() {
        return skinTone;
    }

    public void setSkinTone(SkinTone skinTone) {
        this.skinTone = skinTone;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public HairType getHairType() {
        return hairType;
    }

    public void setHairType(HairType hairType) {
        this.hairType = hairType;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public boolean isFacialHair() {
        return facialHair;
    }

    public void setFacialHair(boolean facialHair) {
        this.facialHair = facialHair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Description{" +
                "age=" + age +
                ", sex=" + sex +
                ", skinTone=" + skinTone +
                ", eyeColor=" + eyeColor +
                ", hairType=" + hairType +
                ", hairColor=" + hairColor +
                ", facialHair=" + facialHair +
                '}';
    }
}
