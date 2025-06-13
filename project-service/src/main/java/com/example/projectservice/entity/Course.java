package com.example.projectservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer courseId;

    String title;
    String description;
    String thumbnailUrl;

    LocalDateTime startDate;
    LocalDateTime endDate;


    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @Builder.Default
    Set<CourseMedia> courseMedia = new HashSet<>();

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    @Builder.Default
    Set<CourseRegistration> courseRegistrations = new HashSet<>();
}
