package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MemberBuyTicketDetailBean;

public interface MemberBuyTicketDetailRepository extends JpaRepository<MemberBuyTicketDetailBean, Integer> {

	Optional<List<MemberBuyTicketDetailBean>> findByMemberBuyTicketOrderId(Integer orderId);

}
