package dev.pdsf.eaglai.model;

import com.fasterxml.jackson.annotation.JsonView;
import dev.pdsf.eaglai.resource.Views;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.time.LocalDate;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Minimal.class)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @JsonView(Views.Minimal.class)
    private String name;

    @NotBlank(message = "Phone number is mandatory")
    @JsonView(Views.Minimal.class)
    private String phone;

    @JsonView(Views.Minimal.class)
    private String nickname;

    @Past(message = "Birthday must be in the past")
    @JsonView(Views.Minimal.class)
    private LocalDate birthday;

    @Column(columnDefinition = "TEXT")
    @JsonView(Views.Minimal.class)
    private String notes;
    
    @Email
    @JsonView(Views.Minimal.class)
    private String email;

    @JsonView(Views.General.class)
    private String address;

    @Lob
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @JsonView(Views.General.class)
    private byte[] imageData;

    @JsonView(Views.General.class)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id", referencedColumnName = "id")
    private Description description;

    protected Contact() {
    }

    public Contact(String name, String email, String phone, String nickname, LocalDate birthday, String notes, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
        this.birthday = birthday;
        this.notes = notes;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setImage(MultipartFile image) throws IOException {
        this.imageData = image.getBytes();
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                ", description=" + description +
                '}';
    }
}
