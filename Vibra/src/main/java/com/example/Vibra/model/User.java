package com.example.Vibra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min=3,max=30)
    @Column(unique=true, nullable=false)
    private String username;

    @NotBlank @Email
    @Column(unique=true, nullable=false)
    private String email;

    @NotBlank @Size(min=6)
    @Column(nullable=false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name="avatar_url")
    private String avatarUrl;

    @Column(length=300)
    private String bio;

    @OneToMany(mappedBy="creator", fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Sound> sounds;

    public User() {}
    public User(Long id, String username, String email, String password) {
        this.id=id; this.username=username; this.email=email; this.password=password;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id=id; }
    public String getUsername() { return username; } public void setUsername(String u) { this.username=u; }
    public String getEmail() { return email; } public void setEmail(String e) { this.email=e; }
    public String getPassword() { return password; } public void setPassword(String p) { this.password=p; }
    public String getAvatarUrl() { return avatarUrl; } public void setAvatarUrl(String a) { this.avatarUrl=a; }
    public String getBio() { return bio; } public void setBio(String b) { this.bio=b; }
    public List<Sound> getSounds() { return sounds; } public void setSounds(List<Sound> s) { this.sounds=s; }
}