package com.kdj.corona.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdj.corona.db.jpa.StatusRepository;
import com.kdj.corona.db.mapper.StatusMapper;
import com.kdj.corona.dto.Status;

@Service
public class StatusService {
	
	@Autowired
	private StatusRepository statusRepository;
	
	public List<Status> getAll() throws Exception{
		return statusRepository.findAllByOrderByDateDesc();
	}
	
	public Status insert(Status status) throws Exception{
		return statusRepository.save(status);
	}

}
