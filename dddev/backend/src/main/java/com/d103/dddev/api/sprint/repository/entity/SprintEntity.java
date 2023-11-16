package com.d103.dddev.api.sprint.repository.entity;

import com.d103.dddev.api.ground.repository.entity.Ground;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sprint")
public class SprintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String goal;
    private Integer status;
    @Column(name="start_date")
    private LocalDate startDate;
    @Column(name="end_date")
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name="ground_id")
    private Ground ground;
    @Column(name = "total_focus_time")
    private Integer totalFocusTime;
    @Column(name = "total_time")
    private Integer totalTime;
}
