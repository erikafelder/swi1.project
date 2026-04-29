package cz.osu.swidemo.dto;

public class UserDTO {
    private String id;
    private String username;
    private Integer age;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    public UserDTO() {
    }

    public UserDTO(String id, String username, Integer age, String email, String firstName, String lastName, String role) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}