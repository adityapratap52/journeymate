package com.journeymate.model.trip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.journeymate.model.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trip_posts")
public class TripPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "uid", columnDefinition = "VARCHAR(255)", updatable = false, nullable = false)
	private String uid = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creater_id", nullable = false)
    private User creater;

    @Column(nullable = false, length = 100)
    private String title;

    private BigDecimal price;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted;
    
    @OneToMany(mappedBy = "tripPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TripImage> images = new ArrayList<>();
    
    @OneToMany(mappedBy = "tripPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TripFeedback> tripFeedback;

    @Column(nullable = false)
    private String description;

    @Column(length = 150)
    private String destination;

    private String preference;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(length = 100)
    private String gender;

    @Column(name = "person_type", length = 100)
    private String personType;

    @Column(name = "person_count")
    private Integer personCount;

    @Column(name = "post_expire_date")
    private LocalDate postExpireDate;

    @Column(name = "trip_starting_date")
    private LocalDate tripStartingDate;

    @Column(name = "trip_ending_date")
    private LocalDate tripEndingDate;
    
    @Column(name = "trip_duration")
    private long tripDuration;

    @Column(name = "trip_transportation", length = 100)
    private String tripTransportation;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}