package com.example.projectservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer registrationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserAccount user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    LocalDateTime enrolledAt;     // Ngày đăng ký
    LocalDateTime cancelledAt;
}
