package dev.pdsf.eaglai.model;

import com.fasterxml.jackson.annotation.JsonView;
import dev.pdsf.eaglai.resource.Views;

import java.time.LocalDate;

import java.time.LocalDate;

public class ContactListDTO {
    @JsonView(Views.Minimal.class)
    private Long id;
    @JsonView(Views.Minimal.class)
    private String name;
    @JsonView(Views.Minimal.class)
    private String phone;
    @JsonView(Views.Minimal.class)
    private String email;
    @JsonView(Views.Minimal.class)
    private String thumbnail;
    @JsonView(Views.Minimal.class)
    private String nickname;
    @JsonView(Views.Minimal.class)
    private LocalDate birthday;

    public ContactListDTO(Long id, String name, String phone, String email, String thumbnail, String nickname, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
        this.birthday = birthday;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getThumbnail() { return thumbnail; }
    public String getNickname() { return nickname; }
    public LocalDate getBirthday() { return birthday; }
}
