package com.journeymate.model.trip;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trip_images")
public class TripImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_post_id", nullable = false)
    private TripPost tripPost;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "uploaded_date")
    private LocalDateTime uploadedDate;

    @PrePersist
    protected void onCreate() {
        uploadedDate = LocalDateTime.now();
    }
}