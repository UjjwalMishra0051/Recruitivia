package com.recruitivia.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity  //this class map to database table
@Table(name = "roles") //table name role
public class Role {

    @Id //marks primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //my sql auto increment
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;   // ADMIN, EMPLOYER or APPLICANT

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}