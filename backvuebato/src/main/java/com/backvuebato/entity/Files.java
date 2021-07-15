package com.backvuebato.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Lob
    private byte[] data;
    private Date insertDate;
    private int whoInserted;
}
