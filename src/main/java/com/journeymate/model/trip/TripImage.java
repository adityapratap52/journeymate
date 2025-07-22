package com.journeymate.model.trip;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trip_images")
public class TripImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "uid", columnDefinition = "VARCHAR(255)", updatable = false, nullable = false)
	private String uid = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TripPost tripPost;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted;

    @Column(name = "uploaded_date")
    private LocalDateTime uploadedDate;

    @PrePersist
    protected void onCreate() {
        uploadedDate = LocalDateTime.now();
    }
}