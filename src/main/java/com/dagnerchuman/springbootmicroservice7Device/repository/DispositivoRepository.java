package com.dagnerchuman.springbootmicroservice7Device.repository;

import com.dagnerchuman.springbootmicroservice7Device.model.Dispositivo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DispositivoRepository extends CrudRepository<Dispositivo, Integer> {

    List<Dispositivo> findByNegocioId(Long negocioId);

    Optional<Dispositivo> findByDeviceId(String deviceId);

}
