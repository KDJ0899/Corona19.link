package com.kdj.corona.db.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdj.corona.dto.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
	List<Status> findAllByOrderByDateDesc();
		
}
