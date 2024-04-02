package kr.basic.abookz.repository;

import kr.basic.abookz.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
  Optional<MemberEntity> findByLoginId(String loginId);
}
