package com.journeymate.model.trip;

import com.journeymate.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "join_requests")
public class JoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "uid", columnDefinition = "VARCHAR(255)", updatable = false, nullable = false)
	private String uid = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_post_id", nullable = false)
    private TripPost tripPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "requester_name", length = 100)
    private String requesterName;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(length = 100)
    private String occupation;

    private String address;

    private Integer age;

    @Column(length = 100)
    private String gender;

    @Column(name = "person_quantity")
    private Integer personQuantity;

    @Column(length = 100)
    private String status;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted;

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