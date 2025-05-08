package com.journeymate.model.trip;

import com.journeymate.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trip_posts")
public class TripPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(length = 150)
    private String destination;

    private BigDecimal amount;

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
    private LocalDateTime postExpireDate;

    @Column(name = "trip_starting_date")
    private LocalDate tripStartingDate;

    @Column(name = "trip_ending_date")
    private LocalDate tripEndingDate;

    @Column(name = "trip_transportation", length = 100)
    private String tripTransportation;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "tripPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripImage> images = new ArrayList<>();

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