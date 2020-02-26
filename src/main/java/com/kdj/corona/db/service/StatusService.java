package com.kdj.corona.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdj.corona.db.mapper.StatusMapper;
import com.kdj.corona.dto.Status;

@Service
public class StatusService {
	
	@Autowired
	StatusMapper statusMapper;
	
	public List<Status> getAll() throws Exception{
		return statusMapper.getAll();
	}
	
	public void insert(Status status) throws Exception{
		statusMapper.insert(status);
	}

}
