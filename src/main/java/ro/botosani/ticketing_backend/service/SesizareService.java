package ro.botosani.ticketing_backend.service;

import org.springframework.stereotype.Service;
import ro.botosani.ticketing_backend.model.Sesizare;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.repository.SesizareRepository;
import java.util.List;

@Service // Această adnotare îi spune lui Spring să instanțieze această clasă
public class SesizareService {

    private final SesizareRepository SesizareRepository;
    private final NotificareService notificareService;

    // Injectăm (aducem) repository-ul prin constructor pentru a-l putea folosi
    public SesizareService(SesizareRepository sesizareRepository, NotificareService notificareService) {
        this.SesizareRepository = sesizareRepository;
        this.notificareService = notificareService;
    }

    // Metodă care întoarce toate tichetele
    public List<Sesizare> iaToateSesizarile() {
        return SesizareRepository.findAll();
    }

    // Metodă care salvează o sesizare
    public Sesizare salveazaSesizareNoua(Sesizare Sesizare) {
        // Dacă am avea validări complexe (ex: câmpuri goale), le-am pune aici.
        return SesizareRepository.save(Sesizare);
    }

    public void stergeSesizare(Long id) {
        Sesizare sesizare = SesizareRepository.findById(id).orElseThrow();
        SesizareRepository.delete(sesizare);
    }

    public Sesizare actualizeazaStareSiMesaj(Long id, String stareNoua, String mesaj) {
        // Căutăm sesizarea în baza de date
        Sesizare sesizare = SesizareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesizarea nu a fost găsită"));

        // Actualizăm câmpurile
        sesizare.setStare(stareNoua);
        sesizare.setMesajDispecer(mesaj);

        // Salvăm modificările
        Sesizare salvata = SesizareRepository.save(sesizare);

        if (salvata.getUtilizator() != null) {
            try {
                notificareService.notificaCetatean(salvata.getUtilizator(), salvata);
            } catch (Exception e) {
                System.err.println("Eroare la trimiterea e-mailului: " + e.getMessage());
            }
        }

        return salvata;
    }
    public List<Sesizare> getSesizariByUser(Long id) {
        return SesizareRepository.findByUtilizatorId(id);
    }

}