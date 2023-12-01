package com.dagnerchuman.springbootmicroservice7Device.controller;

import com.dagnerchuman.springbootmicroservice7Device.model.Dispositivo;
import com.dagnerchuman.springbootmicroservice7Device.service.DispositivoService;
import com.dagnerchuman.springbootmicroservice7Device.utils.DeviceNotFoundException;
import com.dagnerchuman.springbootmicroservice7Device.utils.SendNotification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAllDevices")
    public ResponseEntity<List<Dispositivo>> getAllDevices() {
        List<Dispositivo> dispositivos = service.getAllDevices();
        return new ResponseEntity<>(dispositivos, HttpStatus.OK);
    }

    @GetMapping("/getDevice/{deviceId}")
    public ResponseEntity<?> getDeviceByDeviceId(@PathVariable String deviceId) {
        try {
            Dispositivo dispositivo = service.getDeviceByDeviceId(deviceId);
            return new ResponseEntity<>(dispositivo, HttpStatus.OK);
        } catch (DeviceNotFoundException e) {
            return new ResponseEntity<>("Device not found", HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody SendNotification notification) {
        try {
            service.sendNotificationToAll(notification);
            return new ResponseEntity<>("Notificaciones enviada correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al enviar las notificaciones.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sendNotificationToBusiness/{negocioId}")
    public ResponseEntity<String> sendNotificationToBusiness(
            @PathVariable Long negocioId,
            @RequestBody SendNotification notification) {
        try {
            service.sendNotificationToBusiness(notification, negocioId);
            return new ResponseEntity<>("Notificaciones enviadas correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al enviar las notificaciones.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/updateDevice/{deviceId}")
    public ResponseEntity<Dispositivo> updateDevice(
            @PathVariable int deviceId,
            @RequestBody Dispositivo partialDispositivo) {
        Dispositivo updatedDispositivo = service.updateDevice(deviceId, partialDispositivo);
        return new ResponseEntity<>(updatedDispositivo, HttpStatus.OK);
    }


}
