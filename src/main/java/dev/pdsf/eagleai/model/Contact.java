package dev.pdsf.eagleai.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "contacts")
public class Contact {
    @JsonView(Views.Minimal.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @JsonView(Views.Minimal.class)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @JsonView(Views.Minimal.class)
    @NotBlank(message = "Phone number is mandatory")
    private String phone;

    @JsonView(Views.Minimal.class)
    @Email
    private String email;

    @JsonView(Views.General.class)
    private String address;

    @JsonView(Views.General.class)
    private byte[] imageData;

    @JsonView(Views.General.class)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id", referencedColumnName = "id")
    private Description description;

    protected Contact() {
    }

    public Contact(String name, String email, String phone, String address, byte[] imageData) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.imageData = imageData;
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

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", description=" + description +
                '}';
    }
}
