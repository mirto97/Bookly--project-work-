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

    @Async
    public void inviaMailOTP(String emailDestinatario, String nomeCliente, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("tuamail@gmail.com"); // Assicurati sia la stessa del file properties
        message.setTo(emailDestinatario);
        message.setSubject("Codice di verifica - Cambio Password Bookly");
        
        String testo = "Gentile " + nomeCliente + ",\n\n" +
                    "Hai richiesto il cambio della password. Inserisci il seguente codice nell'app:\n\n" +
                    "CODICE: " + otp + "\n\n" +
                    "Se non hai richiesto tu il cambio, ignora questa mail.";
        
        message.setText(testo);

        try {
            mailSender.send(message);
            System.out.println("Email OTP inviata con successo a: " + emailDestinatario);
        } catch (Exception e) {
            System.err.println("Errore nell'invio della mail OTP: " + e.getMessage());
        }
    }
}