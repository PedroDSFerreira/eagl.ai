package dev.pdsf.eaglai.model;

import java.time.LocalDate;

public class ContactListDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String thumbnail;
    private String nickname;
    private LocalDate birthday;

    public ContactListDTO(Long id, String name, String phone, String email, String thumbnail, String nickname,
            LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
}