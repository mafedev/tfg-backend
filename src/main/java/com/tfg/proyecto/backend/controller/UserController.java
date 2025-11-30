package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.SignupRequest;
import com.tfg.proyecto.backend.entity.User;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api") // Indica que todas las rutas dentro de esta clase empezarán por /api
@CrossOrigin(origins = "*") // Permite que el frontend pueda hacer peticiones desde otro dominio (sin esto, el navegador podría bloquear las peticiones por CORS)
public class UserController {
    // inyección de dependencias
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Crear nuevo usuario
    // Responde a un POST en api/users
    @PostMapping("/users")
    public ResponseEntity<?> createUSer(@RequestBody SignupRequest request){
        // que tenga el email y la contraseña
        if(request.getEmail() == null || request.getPassword() == null){
            return ResponseEntity.badRequest().body("Los campos de email y contraseña son obligtorios");
        }

        // se comprueba que el email no esté registrado
        if(userRepository.existsByEmail((request.getEmail()))){
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        // se crea el usuario que se va a guardar en la bdd
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());

        User guardado = userRepository.save(user);

        // respuesta
        return ResponseEntity.created(URI.create("/api/users" + guardado.getId())).body(guardado);

    }

    // Este método responde a una petición GET, por ejemplo:
    // GET http://localhost:8080/api/users/1
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        // @PathVariable significa que el valor {id} de la URL se pasa al método como parámetro

        // Buscamos el usuario en la base de datos por su ID
        Optional<User> u = userRepository.findById(id);

        // Si se encuentra, lo devolvemos con código 200 OK
        // Si no, devolvemos un 404 Not Found
        return u.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
