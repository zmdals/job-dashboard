package com.jobdashboard.backend.dto.project;

import com.jobdashboard.backend.entity.Project;
import com.jobdashboard.backend.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectReq {

    private String projectName;

    private String role;

    private String techStack;

    private String description;

    public Project toEntity(User user) {
        return Project.builder()
                .user(user)
                .projectName(this.projectName)
                .role(this.role)
                .techStack(this.techStack)
                .description(this.description)
                .build();
    }
}
