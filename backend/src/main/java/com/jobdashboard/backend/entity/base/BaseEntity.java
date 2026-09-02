package com.jobdashboard.backend.entity.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends BaseCreatedEntity{

    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
