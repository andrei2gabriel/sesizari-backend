package ro.botosani.ticketing_backend.model; // Asigură-te că pachetul corespunde cu al tău

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Sesizare") // Numele tabelei în SQLite
public class Sesizare {

    @Id // Marchează acest câmp ca Primary Key (Cheie primară)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Face AUTOINCREMENT automat
    private Long id;

    @Column(nullable = false) // Echivalentul lui NOT NULL în SQL
    private String categorie; // ex: Infrastructura, Salubritate, Iluminat

    @Column(nullable = false, length = 1000)
    private String descriere;

    @Column(nullable = false)
    private String stare; // Nou, In lucru, Rezolvat

    private String adresa; // Poate fi null, deci nu punem @Column restrictiv

    private LocalDateTime dataCreare;

    private Double latitudine;
    private Double longitudine;

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User utilizator;

    public User getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(User utilizator) {
        this.utilizator = utilizator;
    }

    // JPA/Hibernate are nevoie OBLIGATORIU de un constructor gol pentru a putea instanția obiectele din baza de date
    public Sesizare() {
    }

    // Constructorul tău pentru când creezi un tichet nou din cod
    public Sesizare(String categorie, String descriere, String adresa) {
        this.categorie = categorie;
        this.descriere = descriere;
        this.adresa = adresa;
        this.stare = "Nou"; // Default
        this.dataCreare = LocalDateTime.now(); // Ora curentă a serverului
    }

    // --- GETTERI ȘI SETTERI ---
    // Framework-ul are nevoie de ei ca să poată citi și scrie datele în aceste variabile private.

    public Long getId() { return id; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getDescriere() { return descriere; }
    public void setDescriere(String descriere) { this.descriere = descriere; }

    public String getStare() { return stare; }
    public void setStare(String stare) { this.stare = stare; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    public LocalDateTime getDataCreare() { return dataCreare; }
    public void setDataCreare(LocalDateTime dataCreare) { this.dataCreare = dataCreare; }
}