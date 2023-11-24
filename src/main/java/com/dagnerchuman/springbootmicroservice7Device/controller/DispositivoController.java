package com.dagnerchuman.springbootmicroservice7Device.controller;

import com.dagnerchuman.springbootmicroservice7Device.model.Dispositivo;
import com.dagnerchuman.springbootmicroservice7Device.service.DispositivoService;
import com.dagnerchuman.springbootmicroservice7Device.utils.SendNotification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispositivo")
public class DispositivoController {

    private final DispositivoService service;

    public DispositivoController(DispositivoService service) {
        this.service = service;
    }

    @PostMapping("/saveDevice")
    public ResponseEntity<Dispositivo> registerDevice(@RequestBody Dispositivo dispositivo) {
        Dispositivo savedDispositivo = service.registerDevice(dispositivo);
        return new ResponseEntity<>(savedDispositivo, HttpStatus.CREATED);
    }

    @PostMapping("/sendNotification/{deviceId}")
    public ResponseEntity<String> sendNotification(
            @PathVariable int deviceId,
            @RequestBody SendNotification notification) {
        try {
            service.sendNotification(notification, deviceId);
            return new ResponseEntity<>("Notificación enviada correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al enviar la notificación.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
