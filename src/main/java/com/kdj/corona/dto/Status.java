package com.kdj.corona.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {
	private int id;
	private int quarantinedPatient;
	private int treatedPatient;
	private int deceasedPerson;
	private int inspecting;
	private String date;

}
