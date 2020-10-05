package com.backvuebato.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Attributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int ownerid;
    @OneToOne
    @JoinColumn(name = "typeid")
    private Attributetypes attributetypes;
    private String value;
}
