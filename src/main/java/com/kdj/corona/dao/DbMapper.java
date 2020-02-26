package com.kdj.corona.dao;

import java.util.List;

import com.kdj.corona.dto.Status;

public interface DbMapper {
	public List<Status> getList() throws Exception;

}
