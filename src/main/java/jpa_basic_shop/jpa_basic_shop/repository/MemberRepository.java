package jpa_basic_shop.jpa_basic_shop.repository;

import jpa_basic_shop.jpa_basic_shop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);  /** select m from Member m where m.name = ? **/
}
