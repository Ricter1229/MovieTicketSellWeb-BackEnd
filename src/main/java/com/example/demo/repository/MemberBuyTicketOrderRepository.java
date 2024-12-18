package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.MemberBean;
import com.example.demo.domain.MemberBuyTicketDetailBean;
import com.example.demo.domain.MemberBuyTicketOrderBean;
import com.example.demo.dto.internal.ODetailInternalDto;

public interface MemberBuyTicketOrderRepository extends JpaRepository<MemberBuyTicketOrderBean, Integer> {

	Optional<MemberBuyTicketOrderBean> findByMemberId(Integer memberId);

	List<MemberBuyTicketOrderBean> findByState(String string);

	Optional<MemberBuyTicketOrderBean> findById(Integer id);

	@Query("from MemberBuyTicketDetailBean where memberBuyTicketOrderId = :id")
	List<MemberBuyTicketDetailBean> findByMemberBuyTicketOrderId(@Param("id")Integer id);

	
}
