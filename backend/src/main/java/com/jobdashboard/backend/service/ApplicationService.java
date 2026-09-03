package com.jobdashboard.backend.service;

import com.jobdashboard.backend.repository.ApplicationRepository;
import com.jobdashboard.backend.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepositor;
    private final JobPostingRepository jobPostingRepository;


}
