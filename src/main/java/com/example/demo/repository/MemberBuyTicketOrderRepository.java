package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.MemberBuyTicketOrderBean;

public interface MemberBuyTicketOrderRepository extends JpaRepository<MemberBuyTicketOrderBean, Integer> {

	Optional<MemberBuyTicketOrderBean> findByMemberId(Integer memberId);

	List<MemberBuyTicketOrderBean> findByState(String string);

}
