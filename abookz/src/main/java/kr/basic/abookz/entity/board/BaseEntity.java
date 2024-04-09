package kr.basic.abookz.entity.board;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

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
