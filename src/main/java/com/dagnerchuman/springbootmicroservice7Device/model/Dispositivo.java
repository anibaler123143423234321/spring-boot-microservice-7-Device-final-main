package com.dagnerchuman.springbootmicroservice7Device.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Dispositivo")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 400, nullable = true)
    private String deviceId;

    @Column(name = "negocio_id", nullable = true)
    private Long negocioId;
}