package lat.trust.trusttrifles.model;

public class Identity {

    private String dni;
    private String name;
    private String lastname;
    private String email;
    private String phone;

    public Identity(String dni, String name, String lastname, String email, String phone) {
        this.dni = dni;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Identity() {
        this.setPhone("");
        this.setName("");
        this.setLastname("");
        this.setEmail("");
        this.setDni("");
    }
}
