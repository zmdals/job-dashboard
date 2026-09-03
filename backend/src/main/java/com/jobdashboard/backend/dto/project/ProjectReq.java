package com.jobdashboard.backend.dto.project;

import com.jobdashboard.backend.entity.Project;
import com.jobdashboard.backend.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectReq {

    @NotBlank(message = "프로젝트명은 필수입니다.")
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