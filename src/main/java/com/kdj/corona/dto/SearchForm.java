package com.kdj.corona.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchForm {
	@NonNull private String query;
	private int display; //기본값 :10, 최댓값: 100
	private int start; //기본값 :1 , 최댓값:1000
	private String sort;
}
