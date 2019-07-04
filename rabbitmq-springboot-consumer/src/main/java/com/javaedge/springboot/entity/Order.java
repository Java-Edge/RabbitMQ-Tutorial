package com.javaedge.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author JavaEdge
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order implements Serializable {

	private String id;

	private String name;
}
