package ro.botosani.ticketing_backend.dto;

public class AccesRequest {
    private String nume;
    private String prenume;
    private String identificator;

    public AccesRequest() {}

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getIdentificator() { return identificator; }
    public void setIdentificator(String identificator) { this.identificator = identificator; }
}