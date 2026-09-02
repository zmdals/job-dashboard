package com.jobdashboard.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "award_name", nullable = false)
    private String awardName;

    @Column(name = "organizer", nullable = false)
    private String organizer;

    @Column(name = "award_date", nullable = false)
    private LocalDate awardDate;

    @Column(name = "description")
    private String description;

    protected Award() {
        // Default constructor for JPA
    }

}
