package com.dagnerchuman.springbootmicroservice7Device.controller;

import java.net.MalformedURLException;

import com.dagnerchuman.springbootmicroservice7Device.model.Dispositivo;
import com.dagnerchuman.springbootmicroservice7Device.service.DispositivoService;
import com.dagnerchuman.springbootmicroservice7Device.utils.GenericResponse;
import com.dagnerchuman.springbootmicroservice7Device.utils.SendNotification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dispositivo")
public class DispositivoController {

    private final DispositivoService service;

    public DispositivoController(DispositivoService service) {
        this.service = service;
    }

    //GUARDAR DISPOSITIVO
    @PostMapping("/saveDevice")
    public GenericResponse registerDevice(@RequestBody Dispositivo dispositivo) {
        return this.service.registerDevice(dispositivo);
    }

    @PostMapping("/sendNotification/{deviceId}")
    public GenericResponse sendNotification(@PathVariable() int deviceId, @RequestBody SendNotification notification)
            throws MalformedURLException {
        return this.service.sendNotification(notification, deviceId);
    }

}