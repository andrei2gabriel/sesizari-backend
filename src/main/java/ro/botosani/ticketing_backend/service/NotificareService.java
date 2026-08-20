package ro.botosani.ticketing_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ro.botosani.ticketing_backend.model.Sesizare;
import ro.botosani.ticketing_backend.model.User;

@Service
public class NotificareService {

    private final JavaMailSender mailSender;

    public NotificareService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notificaCetatean(User utilizator, Sesizare sesizare) {
        if (utilizator.getEmail() != null && !utilizator.getEmail().isEmpty()) {
            trimiteEmail(utilizator.getEmail(), sesizare);
        } else if (utilizator.getTelefon() != null && !utilizator.getTelefon().isEmpty()) {
            System.out.println("SIMULARE SMS către " + utilizator.getTelefon() + ": Sesizarea dvs. are statusul " + sesizare.getStare());
        }
    }

    private void trimiteEmail(String emailDestinatar, Sesizare sesizare) {
        SimpleMailMessage mesaj = new SimpleMailMessage();
        mesaj.setFrom("sesizaribotosani@gmail.com"); // Trebuie să fie identic cu spring.mail.username
        mesaj.setTo(emailDestinatar);
        mesaj.setSubject("Primăria Botoșani: Actualizare sesizare #" + sesizare.getId());

        String text = "Bună ziua,\n\n"
                + "Sesizarea dumneavoastră din categoria \"" + sesizare.getCategorie() + "\" a fost actualizată.\n\n"
                + "Noul status: " + sesizare.getStare() + "\n"
                + "Mesaj dispecerat: " + (sesizare.getMesajDispecer() != null ? sesizare.getMesajDispecer() : "-") + "\n\n"
                + "O zi excelentă!\nEchipa Primăriei Botoșani";

        mesaj.setText(text);
        mailSender.send(mesaj);
    }
}