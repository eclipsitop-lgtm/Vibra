package com.example.Vibra.service;

import com.example.Vibra.model.Sound;
import com.example.Vibra.model.User;
import com.example.Vibra.repository.SoundRepository;
import com.example.Vibra.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class SoundService {

    // Carpeta donde se guardan los audios en el servidor
    // Cambia esta ruta a donde quieras guardarlos en tu máquina/servidor
    private static final String UPLOAD_DIR = "uploads/sounds/";
    private static final String COVER_DIR  = "uploads/covers/";

    private final SoundRepository soundRepository;
    private final UserRepository  userRepository;

    public SoundService(SoundRepository soundRepository, UserRepository userRepository) {
        this.soundRepository = soundRepository;
        this.userRepository  = userRepository;
    }

    // ── Listar populares
    public List<Sound> getPopular() {
        return soundRepository.findTop20ByOrderByPlayCountDesc();
    }

    // ── Listar recientes
    public List<Sound> getRecent() {
        return soundRepository.findTop20ByOrderByCreatedAtDesc();
    }

    // ── Listar por tipo
    public List<Sound> getByType(String type) {
        return soundRepository.findByTypeOrderByPlayCountDesc(type);
    }

    // ── Buscar
    public List<Sound> search(String q) {
        return soundRepository.search(q);
    }

    // ── Sonidos de un usuario
    public List<Sound> getByCreator(Long userId) {
        return soundRepository.findByCreatorIdOrderByCreatedAtDesc(userId);
    }

    // ── Subir sonido con archivo
    public Sound uploadSound(
            Long creatorId,
            String name,
            String type,
            String genre,
            Double bpm,
            String tags,
            Double price,
            MultipartFile audioFile,
            MultipartFile coverFile
    ) throws IOException {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Guardar archivo de audio
        String audioUrl = saveFile(audioFile, UPLOAD_DIR, new String[]{"audio/mpeg","audio/wav","audio/ogg","audio/flac"});

        // Guardar cover (opcional)
        String coverUrl = null;
        if (coverFile != null && !coverFile.isEmpty()) {
            coverUrl = saveFile(coverFile, COVER_DIR, new String[]{"image/jpeg","image/png","image/webp"});
        }

        Sound sound = new Sound();
        sound.setName(name);
        sound.setType(type);
        sound.setGenre(genre);
        sound.setBpm(bpm);
        sound.setTags(tags);
        sound.setPrice(price);
        sound.setAudioUrl(audioUrl);
        sound.setCoverUrl(coverUrl);
        sound.setCreator(creator);

        return soundRepository.save(sound);
    }

    // ── Incrementar play count
    public void incrementPlay(Long id) {
        soundRepository.findById(id).ifPresent(s -> {
            s.setPlayCount(s.getPlayCount() + 1);
            soundRepository.save(s);
        });
    }

    // ── Guardar archivo en disco
    private String saveFile(MultipartFile file, String dir, String[] allowedTypes) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo vacío");
        }

        // Validar tipo MIME
        String contentType = file.getContentType();
        boolean allowed = false;
        for (String t : allowedTypes) { if (t.equals(contentType)) { allowed = true; break; } }
        if (!allowed) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de archivo no permitido: " + contentType);

        // Crear carpeta si no existe
        Path uploadPath = Paths.get(dir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        // Nombre único para evitar colisiones
        String ext      = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + ext;
        Path   filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Retorna URL relativa (el controller la expone como recurso estático)
        return "/" + dir + filename;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}