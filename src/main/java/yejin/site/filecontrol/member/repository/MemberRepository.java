package yejin.site.filecontrol.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yejin.site.filecontrol.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

}
