package com.softedge.solution.repomodels;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "location_tbl")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToOne
    @JoinColumn(name = "state_id")
    private State state;

    private String city;

    private String pincode;

    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserRegistration user;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;


}
