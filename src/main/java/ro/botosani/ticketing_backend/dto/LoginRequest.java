package ro.botosani.ticketing_backend.dto;

public class LoginRequest {
    private String identificator;
    private String password;

    public LoginRequest() {
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public String getIdentificator() {
        return identificator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
