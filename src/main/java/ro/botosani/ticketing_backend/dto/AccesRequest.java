package ro.botosani.ticketing_backend.dto;

public class AccesRequest {
    private String nume;
    private String prenume;
    private String identificator;
    private String captchaToken;

    public AccesRequest() {}

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getCaptchaToken() {
        return captchaToken;
    }

    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    public String getIdentificator() { return identificator; }
    public void setIdentificator(String identificator) { this.identificator = identificator; }
}