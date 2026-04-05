package com.abdelrahman.backend.entity;

import com.abdelrahman.backend.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean archived;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
}