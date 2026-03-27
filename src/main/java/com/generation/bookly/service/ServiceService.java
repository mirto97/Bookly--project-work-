package com.generation.bookly.service;

import com.generation.bookly.model.Service;
import com.generation.bookly.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService
{

    private final ServiceRepository serviceRepository;

    // Recupera tutti i servizi nel database
    public List<Service> findAll() 
    {
        return serviceRepository.findAll();
    }

    // Recupera i servizi filtrati per ID dell'attività (Business)
    public List<Service> findByActivityId(int activityId) 
    {
        // Assicurati che nel ServiceRepository esista il metodo:
        // List<Service> findByActivityId(int activityId);
        return serviceRepository.findByActivityId(activityId);
    }

    // Trova un singolo servizio per ID
    public Service findById(int id) 
    {
        Optional<Service> optionalService = serviceRepository.findById(id);
        return optionalService.orElse(null);
    }

    // Salva o aggiorna un servizio
    public Service save(Service service) 
    {
        return serviceRepository.save(service);
    }

    // Aggiorna i campi modificabili di un servizio esistente
    public Service updateById(int id, Service patch)
    {
        Service existing = serviceRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setName(patch.getName());
        existing.setDescription(patch.getDescription());
        existing.setDuration(patch.getDuration());
        existing.setPrice(patch.getPrice());
        existing.setNotes(patch.getNotes());
        return serviceRepository.save(existing);
    }

    // Elimina un servizio per ID
    public void deleteById(int id)
    {
        serviceRepository.deleteById(id);
    }
}