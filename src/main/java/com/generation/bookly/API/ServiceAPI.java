package com.generation.bookly.API;

import com.generation.bookly.model.Service;
import com.generation.bookly.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:4200") // Utile per collegare il frontend senza errori CORS
public class ServiceAPI {

    @Autowired
    private ServiceService serviceService;

    // 1. Ottieni tutti i servizi
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return new ResponseEntity<>(serviceService.findAll(), HttpStatus.OK);
    }

    // 2. Ottieni i servizi di una specifica attività
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Service>> getServicesByActivity(@PathVariable int activityId) {
        return new ResponseEntity<>(serviceService.findByActivityId(activityId), HttpStatus.OK);
    }

    // 3. Ottieni un singolo servizio per ID
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable int id) {
        Service s = serviceService.findById(id);
        if (s == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    // 4. Crea un nuovo servizio
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        try {
            return new ResponseEntity<>(serviceService.save(service), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 5. Elimina un servizio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable int id) {
        serviceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}