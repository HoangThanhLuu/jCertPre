package com.app.elearningservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelCourse {
    private int levelId;
    private String name;
    private String status;

    public LevelCourse(int levelId, String name) {
        this.levelId = levelId;
        this.name = name;
        this.status = "Active"; // Default status
    }
}
