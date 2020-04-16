package com.kdj.corona.dto;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Status {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int id;
	@Column
	private int quarantinedPatient;
	@Column
	private int treatedPatient;
	@Column
	private int deceasedPerson;
	@Column
	private int inspecting;
	@Column
	private LocalDateTime date;

}
