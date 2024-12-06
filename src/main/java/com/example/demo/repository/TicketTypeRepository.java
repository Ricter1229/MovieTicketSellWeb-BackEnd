package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.TicketTypeBean;

public interface TicketTypeRepository extends JpaRepository<TicketTypeBean, Integer> {

}
