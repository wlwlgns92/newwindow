package ansan.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseTimeEntity {
    // 엔티티 생성날짜/수정날짜 자동주입

    @CreatedDate
    private LocalDateTime createdDate; // 객체

    @LastModifiedDate
    private LocalDateTime modifiedDate;



}
