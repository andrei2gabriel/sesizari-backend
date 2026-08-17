package ro.botosani.ticketing_backend.service;

import org.springframework.stereotype.Service;
import ro.botosani.ticketing_backend.model.Sesizare;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.repository.SesizareRepository;
import java.util.List;

@Service // Această adnotare îi spune lui Spring să instanțieze această clasă
public class SesizareService {

    private final SesizareRepository SesizareRepository;

    // Injectăm (aducem) repository-ul prin constructor pentru a-l putea folosi
    public SesizareService(SesizareRepository sesizareRepository) {
        this.SesizareRepository = sesizareRepository;
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


    public List<Sesizare> getSesizariByUser(Long id) {
        return SesizareRepository.findByUtilizatorId(id);
    }

}