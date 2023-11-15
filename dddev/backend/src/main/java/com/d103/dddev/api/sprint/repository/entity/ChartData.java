package com.d103.dddev.api.sprint.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.d103.dddev.api.ground.repository.entity.Ground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "chart_data")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ground_id")
	private Ground ground;


	@ManyToOne
	@JoinColumn(name = "sprint_id")
	private SprintEntity sprint;

	@Column(name = "time_type")
	private String timeType;

	@Column(name = "is_done")
	private Boolean isDone;

	private Integer time;

	private Integer count;
}
