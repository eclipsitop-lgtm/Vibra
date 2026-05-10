package com.example.Vibra.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sounds")
public class Sound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;           // "Dark Trap Kit"

    @Column(nullable = false)
    private String type;           // drum | melody | sample | vocal | loop | bass

    private String genre;          // trap, lofi, drill…

    private Double bpm;

    private String tags;           // "Trap,140BPM" (CSV)

    private Double price;          // null o 0.0 = gratis

    @Column(name = "audio_url")
    private String audioUrl;       // ruta o URL del archivo

    @Column(name = "cover_url")
    private String coverUrl;       // imagen de portada (opcional)

    @Column(name = "play_count")
    private Long playCount = 0L;

    @Column(name = "download_count")
    private Long downloadCount = 0L;

    // Relación con el usuario que sube el sonido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ─── Constructors ───
    public Sound() {}

    // ─── Getters & Setters ───
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Double getBpm() { return bpm; }
    public void setBpm(Double bpm) { this.bpm = bpm; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public Long getPlayCount() { return playCount; }
    public void setPlayCount(Long playCount) { this.playCount = playCount; }

    public Long getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Long downloadCount) { this.downloadCount = downloadCount; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}