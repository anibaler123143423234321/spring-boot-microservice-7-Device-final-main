package com.dagnerchuman.springbootmicroservice7Device.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="Dispositivo")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 400)
    private String deviceId;

}