package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.RelationRequest;
import com.tfg.proyecto.backend.dto.response.RelationResponse;
import com.tfg.proyecto.backend.entity_model.Relation;
import com.tfg.proyecto.backend.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relations")
public class RelationController {

    private final RelationService relationService;

    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    @PostMapping("/generate")
    public RelationResponse generateRelation(@AuthenticationPrincipal UserDetails user){
        Long caregiverId = Long.valueOf(user.getUsername());
        Relation relation = relationService.createRelation(caregiverId);

        return new RelationResponse(relation.getCode());
    }

    @PostMapping("/link")
    public RelationResponse linkPatient(@RequestBody RelationRequest request, @AuthenticationPrincipal UserDetails user){
        Long patientId = Long.parseLong(user.getUsername());
        Relation relation = relationService.linkPatient(request.getCode(), patientId);
        return new RelationResponse(relation.getCode());
    }
}
