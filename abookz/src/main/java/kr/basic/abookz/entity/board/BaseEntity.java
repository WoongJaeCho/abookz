package kr.basic.abookz.entity.board;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

// 생성시 및 수정할때 필요한 클래스
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
  @CreationTimestamp
  @Column(updatable = false)
  private LocalDate createDate;

  @UpdateTimestamp
  @Column(insertable = false)
  private LocalDate updateDate;
}
