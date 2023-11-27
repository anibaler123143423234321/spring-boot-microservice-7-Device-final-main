package com.dagnerchuman.springbootmicroservice7Device.service;

import com.dagnerchuman.springbootmicroservice7Device.model.Dispositivo;
import com.dagnerchuman.springbootmicroservice7Device.repository.DispositivoRepository;
import com.dagnerchuman.springbootmicroservice7Device.utils.DeviceNotFoundException;
import com.dagnerchuman.springbootmicroservice7Device.utils.SendNotification;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DispositivoService {

    private final DispositivoRepository repository;

    private static final String FCM_APIKEY = "AAAA8W8-qw0:APA91bGW9X7hE1_6ZaL3wAvYsB1dyUDO9FGQRofVqUD7mERypoz_598Fk7NzW7vt8oroX7GGtMyF-gzCrD44S-aHbZ1erLgn8Cuc4ad9ocErAfh4JfPrzmztrablBJYzB06Cu7X5G0sN";
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final int HTTP_OK = 200;

    public DispositivoService(DispositivoRepository repository) {
        this.repository = repository;
    }

    public Dispositivo registerDevice(Dispositivo dispositivo) {
        return repository.save(dispositivo);
    }

    public void sendNotification(SendNotification notification, int deviceID) {
        Optional<Dispositivo> dispositivoOptional = repository.findById(deviceID);

        if (dispositivoOptional.isPresent()) {
            Dispositivo dispositivo = dispositivoOptional.get();
            notification.setId(dispositivo.getDeviceId());

            try {
                URL url = new URL(FCM_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key=" + FCM_APIKEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String body = buildNotificationBody(notification);

                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes());
                os.flush();

                int responseCode = conn.getResponseCode();
                if (responseCode == HTTP_OK) {
                    System.out.println("Notificación enviada correctamente.");
                } else {
                    System.out.println("Error al enviar la notificación. Código de respuesta: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("El deviceID no ha sido encontrado");
        }
    }

    private static String buildNotificationBody(SendNotification notification) {
        return String.format(
                "{\"to\":\"%s\",\"notification\":{\"title\":\"%s\",\"body\":\"%s\"}}",
                notification.getId(), notification.getTitle(), notification.getMessage()
        );
    }

    public List<Dispositivo> getAllDevices() {
        Iterable<Dispositivo> dispositivosIterable = repository.findAll();
        List<Dispositivo> dispositivos = new ArrayList<>();

        dispositivosIterable.forEach(dispositivos::add);

        return dispositivos;
    }

    public void sendNotificationToAll(SendNotification notification) {
        List<Dispositivo> dispositivos = getAllDevices();

        for (Dispositivo dispositivo : dispositivos) {
            notification.setId(dispositivo.getDeviceId());
            sendNotification(notification, dispositivo.getId());
        }
    }

    public void sendNotificationToBusiness(SendNotification notification, Long negocioId) {
        List<Dispositivo> dispositivos = repository.findByNegocioId(negocioId);

        for (Dispositivo dispositivo : dispositivos) {
            try {
                sendNotification(notification, dispositivo.getId());
            } catch (Exception e) {
                // Registra el error y continúa con el siguiente dispositivo
                System.err.println("Error al enviar notificación al dispositivo " + dispositivo.getId() + ": " + e.getMessage());
            }
        }
    }




    public Dispositivo updateDevice(int deviceId, Dispositivo partialDispositivo) {
        Optional<Dispositivo> dispositivoOptional = repository.findById(deviceId);

        if (dispositivoOptional.isPresent()) {
            Dispositivo existingDispositivo = dispositivoOptional.get();

            if (partialDispositivo.getDeviceId() != null) {
                existingDispositivo.setDeviceId(partialDispositivo.getDeviceId());
            }

            if (partialDispositivo.getNegocioId() != null) {
                existingDispositivo.setNegocioId(partialDispositivo.getNegocioId());
            }

            return repository.save(existingDispositivo);
        } else {
            throw new DeviceNotFoundException("Device con ID " + deviceId + " no encontrado");
        }
    }
}
