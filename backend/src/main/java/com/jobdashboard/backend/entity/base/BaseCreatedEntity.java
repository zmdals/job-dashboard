package com.jobdashboard.backend.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass // 이 클래스는 실제 엔티티가 아니고, 여기 필드를 상속받은 엔티티의 테이블 컬럼으로 내려보낸다.

//BaseEntity는 그냥 생성,수정일자를 저장하기 위함, 테이블이 될 이유가 없음.
// abstract로 new BaseEntity() 막아둠.
public abstract class BaseCreatedEntity {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;
}
