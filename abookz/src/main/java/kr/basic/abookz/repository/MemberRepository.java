package kr.basic.abookz.repository;

import kr.basic.abookz.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
  Optional<MemberEntity> findByLoginId(String loginId);
  Optional<MemberEntity> findByEmail(String email);


  boolean existsByLoginId(String loginId);

  Optional<MemberEntity> findByProviderAndProviderId(String provider , String providerId);

  Optional<MemberEntity> findByLoginIdAndEmail(String loginId, String email);

}
