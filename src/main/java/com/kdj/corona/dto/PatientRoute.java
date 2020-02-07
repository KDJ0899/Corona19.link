package com.kdj.corona.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRoute {
	private int id;
	private int number;
	private double latitude;
	private double longitude;
	private String content;
}
