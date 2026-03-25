package com.generation.bookly.service;

import com.generation.bookly.model.Service;
import com.generation.bookly.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    // Recupera tutti i servizi nel database
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    // Recupera i servizi filtrati per ID dell'attività (Business)
    public List<Service> findByActivityId(int activityId) {
        // Assicurati che nel ServiceRepository esista il metodo:
        // List<Service> findByActivityId(int activityId);
        return serviceRepository.findByActivityId(activityId);
    }

    // Trova un singolo servizio per ID
    public Service findById(int id) {
        Optional<Service> optionalService = serviceRepository.findById(id);
        return optionalService.orElse(null);
    }

    // Salva o aggiorna un servizio
    public Service save(Service service) {
        return serviceRepository.save(service);
    }

    // Elimina un servizio per ID
    public void deleteById(int id) {
        serviceRepository.deleteById(id);
    }
}