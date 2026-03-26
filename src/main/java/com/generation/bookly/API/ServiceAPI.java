package com.generation.bookly.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.bookly.dto.ServiceDTO;
import com.generation.bookly.mapper.ServiceMapper;
import com.generation.bookly.model.Service;
import com.generation.bookly.service.ServiceService;

@RestController
@RequestMapping("/bookly/api/services")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceAPI 
{

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceMapper serviceMapper;

    // 1. Ottieni tutti i servizi (Mappati a DTO)
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() 
    {
        List<ServiceDTO> dtos = serviceService.findAll()
                .stream()
                .map(serviceMapper::toDTO) // Assumendo un costruttore che accetta l'Entity
                .toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 2. Ottieni per attività
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<ServiceDTO>> getServicesByActivity(@PathVariable int activityId) 
    {
        List<ServiceDTO> dtos = serviceService.findByActivityId(activityId)
                .stream()
                .map(serviceMapper::toDTO)
                .toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 3. Ottieni singolo (Mappato a DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable int id) 
    {
        Service s = serviceService.findById(id);
        if(s == null) 
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        ServiceDTO dto = serviceMapper.toDTO(s);
        return new ResponseEntity<ServiceDTO>(dto, HttpStatus.OK);
    }

    // 4. Crea un nuovo servizio (Riceve DTO o Entity, restituisce DTO)
    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@RequestBody Service service) 
    {
        try 
        {
            Service saved = serviceService.save(service);
            ServiceDTO dto = serviceMapper.toDTO(saved);
            return new ResponseEntity<ServiceDTO>(dto, HttpStatus.OK);
        } 
        catch (Exception e) 
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable int id) 
    {
        serviceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}