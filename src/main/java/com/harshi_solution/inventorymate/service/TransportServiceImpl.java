package com.harshi_solution.inventorymate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harshi_solution.inventorymate.entities.Transport;
import com.harshi_solution.inventorymate.repo.TransportRepository;

@Service
public class TransportServiceImpl implements TransportService {

	@Autowired
	private TransportRepository transportRepository;

	@Override
	public Transport saveTransport(Transport transport) {

		return transportRepository.save(transport);
	}

	@Override
	public List<Transport> getTransportList() {

		return transportRepository.findAll();
	}

	@Override
	public Transport getTransportById(Long selectedTransportId) {

		return transportRepository.findById(selectedTransportId).orElse(null);
	}

}
