package com.example.demo.service.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.TicketTypeBean;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.TicketTypeRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class TicketTypeService {
	@Autowired
	private TicketTypeRepository ticketTypeRepository;
	
	public TicketTypeBean findOneType(Integer id) {
		return ticketTypeRepository.findById(id)
			.orElseThrow(() -> new CustomException("Ticket type not found", 404));
	}
	
	public List<TicketTypeBean> findAllType() {
		List<TicketTypeBean> tickets = ticketTypeRepository.findAll();
		if(tickets == null || tickets.size() == 0) {
			throw new CustomException("Ticket type not found", 404);
		}
		return tickets;
	}
}
