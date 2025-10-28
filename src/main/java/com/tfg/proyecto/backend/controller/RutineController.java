package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.RutineRequest;
import com.tfg.proyecto.backend.entity.Rutine;
import com.tfg.proyecto.backend.repository.RutineRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RutineController {

    private final RutineRepository rutineRepository;

    public RutineController(RutineRepository rutineRepository) {
        this.rutineRepository = rutineRepository;
    }

    @PostMapping("/rutine")
    public ResponseEntity<Rutine> crearRutina(@RequestBody RutineRequest request) {
        Rutine rutine = new Rutine(
                request.getBreakfastTime(),
                request.getLunchTime(),
                request.getDinnerTime(),
                request.getBathTime(),
                request.getEnableHydration(),
                request.getHydrationIntervalHours()
        );

        Rutine saved = rutineRepository.save(rutine);
        return ResponseEntity.ok(saved);
    }
}

