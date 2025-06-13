package com.example.projectservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "course_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer mediaId;
    String mediaType; // e.g., "video", "audio", "document"
    String mediaUrl;
    String title;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    Course course;
}
