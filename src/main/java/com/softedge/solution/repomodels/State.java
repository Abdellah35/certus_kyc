package com.softedge.solution.repomodels;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "state_mtb")
@Data
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "state_name")
    private String StateName;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToOne(mappedBy = "state")
    private Location location;
}
