package com.javaedge.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author JavaEdge
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

	private String id;
	
	private String name;
	
	private String content;
}

