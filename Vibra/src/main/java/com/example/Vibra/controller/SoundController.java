package com.example.Vibra.controller;

import com.example.Vibra.model.Sound;
import com.example.Vibra.service.SoundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sounds")
@CrossOrigin("*")
public class SoundController {

    private final SoundService soundService;

    public SoundController(SoundService soundService) {
        this.soundService = soundService;
    }

    // GET /sounds/popular
    @GetMapping("/popular")
    public List<Sound> getPopular() {
        return soundService.getPopular();
    }

    // GET /sounds/recent
    @GetMapping("/recent")
    public List<Sound> getRecent() {
        return soundService.getRecent();
    }

    // GET /sounds/type/{type}  →  drum | melody | sample | vocal | loop | bass
    @GetMapping("/type/{type}")
    public List<Sound> getByType(@PathVariable String type) {
        return soundService.getByType(type);
    }

    // GET /sounds/search?q=trap
    @GetMapping("/search")
    public List<Sound> search(@RequestParam String q) {
        return soundService.search(q);
    }

    // GET /sounds/creator/{userId}
    @GetMapping("/creator/{userId}")
    public List<Sound> getByCreator(@PathVariable Long userId) {
        return soundService.getByCreator(userId);
    }

    // POST /sounds/upload  →  multipart/form-data
    @PostMapping("/upload")
    public ResponseEntity<Sound> upload(
            @RequestParam("creatorId") Long creatorId,
            @RequestParam("name")      String name,
            @RequestParam("type")      String type,
            @RequestParam(value="genre", required=false) String genre,
            @RequestParam(value="bpm",   required=false) Double bpm,
            @RequestParam(value="tags",  required=false) String tags,
            @RequestParam(value="price", required=false, defaultValue="0") Double price,
            @RequestParam("audio")     MultipartFile audioFile,
            @RequestParam(value="cover", required=false) MultipartFile coverFile
    ) throws IOException {
        Sound s = soundService.uploadSound(creatorId, name, type, genre, bpm, tags, price, audioFile, coverFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    // POST /sounds/{id}/play  →  incrementar reproducciones
    @PostMapping("/{id}/play")
    public ResponseEntity<Void> incrementPlay(@PathVariable Long id) {
        soundService.incrementPlay(id);
        return ResponseEntity.ok().build();
    }
}