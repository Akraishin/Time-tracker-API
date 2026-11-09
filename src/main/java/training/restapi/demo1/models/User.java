package training.restapi.demo1.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Boolean isActive = true;

    private String name;

    private String email;

    @OneToMany(mappedBy = "user")
    private Set<TimeLog> timeLogs = new HashSet<>();

    public User() {
    }

    public User(Long id, String username, String name, String email, Boolean isActive) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<TimeLog> getTimeLogs() {
        return timeLogs;
    }

    public void setTimeLogs(Set<TimeLog> timeLogs) {
        this.timeLogs = timeLogs;
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
}
