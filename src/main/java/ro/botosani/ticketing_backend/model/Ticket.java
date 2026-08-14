package ro.botosani.ticketing_backend.model; // Asigură-te că pachetul corespunde cu al tău

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tichete") // Numele tabelei în SQLite
public class Ticket {

    @Id // Marchează acest câmp ca Primary Key (Cheie primară)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Face AUTOINCREMENT automat
    private Long id;

    @Column(nullable = false) // Echivalentul lui NOT NULL în SQL
    private String categorie; // ex: Infrastructura, Salubritate, Iluminat

    @Column(nullable = false, length = 1000)
    private String descriere;

    @Column(nullable = false)
    private String stare; // Nou, In lucru, Rezolvat

    private String locatie; // Poate fi null, deci nu punem @Column restrictiv

    private LocalDateTime dataCreare;

    // JPA/Hibernate are nevoie OBLIGATORIU de un constructor gol pentru a putea instanția obiectele din baza de date
    public Ticket() {
    }

    // Constructorul tău pentru când creezi un tichet nou din cod
    public Ticket(String categorie, String descriere, String locatie) {
        this.categorie = categorie;
        this.descriere = descriere;
        this.locatie = locatie;
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

    public String getLocatie() { return locatie; }
    public void setLocatie(String locatie) { this.locatie = locatie; }

    public LocalDateTime getDataCreare() { return dataCreare; }
    public void setDataCreare(LocalDateTime dataCreare) { this.dataCreare = dataCreare; }
}