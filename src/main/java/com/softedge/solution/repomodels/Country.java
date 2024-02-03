package com.softedge.solution.repomodels;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="country_mtb")
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "country_logo")
    private String countryLogo;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "country")
    private Location location;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "country",
            cascade = CascadeType.ALL)
    private State state;

}
