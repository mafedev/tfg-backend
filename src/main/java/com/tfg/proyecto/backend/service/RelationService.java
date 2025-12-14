package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.entity_model.Relation;
import com.tfg.proyecto.backend.repository.RelationRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    // Genera el código que introduce el paciente
    private String generateCode(){
        String code;
        do{
            code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        }while(relationRepository.existsByCode(code));
        return code;
    }

    // Crea la "relación", solo con el id del cuidador y el código
    // para que luego cuando el paciente lo ingrese se agrega a la fila/relación
    public Relation createRelation(Long caregiverId){
        Relation relation = new Relation();

        relation.setCaregiverId(caregiverId);
        relation.setCode(generateCode());

        return relationRepository.save(relation);
    }

    // se llama cuando el paciente ingresa el código
    public Relation linkPatient(String code, Long patientId){
        Optional<Relation> relation = relationRepository.findByCode(code); // obtiene la relación con ese código si existe

        if(relation.isPresent()){ // si existe
            Relation row = relation.get(); // obtiene la fila
            row.setPatientId(patientId); // pone el id del paciente
            return relationRepository.save(row); // lo guarda
        } else { // si no lanza una excepción
            throw new RuntimeException("El código no existe");
        }
    }

}
