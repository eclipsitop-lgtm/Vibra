package com.example.Vibra.repository;

import com.example.Vibra.model.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SoundRepository extends JpaRepository<Sound, Long> {

    // Por tipo: drum, melody, sample…
    List<Sound> findByTypeOrderByPlayCountDesc(String type);

    // Por creador
    List<Sound> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);

    // Populares (todos los tipos)
    List<Sound> findTop20ByOrderByPlayCountDesc();

    // Recientes
    List<Sound> findTop20ByOrderByCreatedAtDesc();

    // Búsqueda por nombre o tags
    @Query("SELECT s FROM Sound s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%',:q,'%')) " +
            "OR LOWER(s.tags) LIKE LOWER(CONCAT('%',:q,'%')) " +
            "OR LOWER(s.genre) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Sound> search(String q);
}
