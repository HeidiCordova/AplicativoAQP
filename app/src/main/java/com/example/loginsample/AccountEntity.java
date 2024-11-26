package com.example.loginsample;

public class AccountEntity {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String username;
    private String password;
    private int id;  // Nueva variable para el identificador único

    // Constructor vacío
    public AccountEntity() {
    }

    // Constructor con parámetros
    public AccountEntity(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    // Getters y Setters
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Método adicional para obtener el nombre completo
    public String getNombre() {
        return firstname + " " + lastname;
    }

    // Métodos para ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
