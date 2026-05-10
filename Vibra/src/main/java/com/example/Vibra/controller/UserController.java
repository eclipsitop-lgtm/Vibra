package com.example.Vibra.controller;

import com.example.Vibra.model.User;
import com.example.Vibra.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
    private final UserService svc;
    // Directorio relativo a la raíz del proyecto
    private static final String AVATAR_DIR = "uploads/avatars/";

    public UserController(UserService svc) { this.svc = svc; }

    @GetMapping public List<User> all() { return svc.getUsers(); }


    @PostMapping
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String,String> b) {
        return ResponseEntity.ok(svc.login(b.get("email"), b.get("password")));
    }

    @PatchMapping("/{id}/avatar")
    public ResponseEntity<User> avatar(@PathVariable Long id, @RequestParam("file") MultipartFile f) throws IOException {
        Path dir = Paths.get(AVATAR_DIR);
        if (!Files.exists(dir)) Files.createDirectories(dir);

        // Generar nombre único con extensión original
        String ext = ".jpg";
        if (f.getOriginalFilename() != null && f.getOriginalFilename().contains(".")) {
            ext = f.getOriginalFilename().substring(f.getOriginalFilename().lastIndexOf("."));
        }
        String fn = UUID.randomUUID().toString() + ext;

        // Guardar archivo físico
        Files.copy(f.getInputStream(), dir.resolve(fn), StandardCopyOption.REPLACE_EXISTING);

        // Guardar en BD como "/uploads/avatars/nombre.jpg"
        String dbPath = "/uploads/avatars/" + fn;
        return ResponseEntity.ok(svc.updateAvatar(id, dbPath));
    }

    @PatchMapping("/{id}/bio")
    public ResponseEntity<User> bio(@PathVariable Long id, @RequestBody Map<String,String> b) {
        return ResponseEntity.ok(svc.updateBio(id, b.get("bio")));
    }
}