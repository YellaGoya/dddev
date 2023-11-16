package com.d103.dddev.api.ground.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.sprint.TimeType;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;

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
	@Enumerated(EnumType.STRING)
	private TimeType timeType;

	private Integer time;

}
