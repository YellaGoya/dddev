package com.d103.dddev.api.ground.repository.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "ground_user")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@DynamicInsert
@AllArgsConstructor
public class GroundUserDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserDto userDto;

	@ManyToOne
	@JoinColumn(name = "ground_id")
	private GroundDto groundDto;

	@Column(name = "is_owner")
	private Boolean isOwner;
}
