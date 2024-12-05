package tw.com.ispan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.MemberBean;

public interface MemberRepository extends JpaRepository<MemberBean, Integer>, CustomMemberRepository {
    boolean existsByAccountOrEmail(String account, String email);

    Optional<MemberBean> findByEmail(String email);

    Optional<MemberBean> findByAccount(String account);
}
