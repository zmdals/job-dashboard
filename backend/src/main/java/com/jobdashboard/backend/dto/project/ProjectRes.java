package com.jobdashboard.backend.dto.project;

import com.jobdashboard.backend.entity.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectRes {

    private Long id;

    private String projectName;

    private String role;

    private String techStack;

    private String description;

    public static ProjectRes from(Project project) {
        return ProjectRes.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .role(project.getRole())
                .techStack(project.getTechStack())
                .description(project.getDescription())
                .build();
    }
}
