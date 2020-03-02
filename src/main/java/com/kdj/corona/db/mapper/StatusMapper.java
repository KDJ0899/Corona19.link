package com.kdj.corona.db.mapper;

import java.util.List;

import com.kdj.corona.dto.Status;

public interface StatusMapper {
	public List<Status> getAll() throws Exception;
	public int insert(Status status) throws Exception;

}
