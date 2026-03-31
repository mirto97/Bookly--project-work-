package com.generation.bookly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async // Esegue il metodo in modo asincrono, non blocca il thread principale
    public void inviaMailConferma(String emailDestinatario, String nomeCliente, String infoPrenotazione) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        // Deve corrispondere alla mail configurata in application.properties
        message.setFrom("tuamail@gmail.com"); 
        message.setTo(emailDestinatario);
        message.setSubject("Conferma Prenotazione - Progetto Accademico");
        
        String testo = "Gentile " + nomeCliente + ",\n\n" +
                       "Ti confermiamo che la tua prenotazione è stata registrata.\n" +
                       "Dettagli: " + infoPrenotazione + "\n\n" +
                       "Grazie per aver usato la nostra App!";
        
        message.setText(testo);

        try {
            mailSender.send(message);
            System.out.println("Email inviata con successo a: " + emailDestinatario);
        } catch (Exception e) {
            System.err.println("Errore nell'invio della mail: " + e.getMessage());
        }
    }
}