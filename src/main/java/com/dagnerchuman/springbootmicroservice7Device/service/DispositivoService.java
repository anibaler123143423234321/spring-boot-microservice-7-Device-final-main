package com.dagnerchuman.springbootmicroservice7Device.service;

import static com.dagnerchuman.springbootmicroservice7Device.utils.Global.OPERACION_CORRECTA;
import static com.dagnerchuman.springbootmicroservice7Device.utils.Global.OPERACION_ERRONEA;
import static com.dagnerchuman.springbootmicroservice7Device.utils.Global.RPTA_ERROR;
import static com.dagnerchuman.springbootmicroservice7Device.utils.Global.RPTA_OK;
import static com.dagnerchuman.springbootmicroservice7Device.utils.Global.RPTA_WARNING;
import static com.dagnerchuman.springbootmicroservice7Device.utils.Global.TIPO_DATA;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import com.dagnerchuman.springbootmicroservice7Device.model.Dispositivo;
import com.dagnerchuman.springbootmicroservice7Device.repository.DispositivoRepository;
import com.dagnerchuman.springbootmicroservice7Device.utils.GenericResponse;
import com.dagnerchuman.springbootmicroservice7Device.utils.SendNotification;
import org.springframework.stereotype.Service;

    @Service
    public class DispositivoService {

        private final DispositivoRepository repository;

        private static final String FCM_APIKEY =
                "AAAA8W8-qw0:APA91bGW9X7hE1_6ZaL3wAvYsB1dyUDO9FGQRofVqUD7mERypoz_598Fk7NzW7vt8oroX7GGtMyF-gzCrD44S-aHbZ1erLgn8Cuc4ad9ocErAfh4JfPrzmztrablBJYzB06Cu7X5G0sN";

        private static final String URL = "https://fcm.googleapis.com/fcm/send";

        public DispositivoService(DispositivoRepository repository) {
            this.repository = repository;
        }

        public GenericResponse<Dispositivo> registerDevice(Dispositivo dispositivo) {
            return new GenericResponse(TIPO_DATA, RPTA_OK, OPERACION_CORRECTA,
                    this.repository.save(dispositivo));
        }

        public GenericResponse sendNotification(SendNotification notification, int deviceID) throws MalformedURLException {
            Optional<Dispositivo> dispositivo = this.repository.findById(deviceID);
            if (dispositivo.isPresent()) {
                notification.setId(dispositivo.get().getDeviceId());
                try {
                    URL url = new URL(URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // Configurar la conexión
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "key=" + FCM_APIKEY);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);
                    // Construir el cuerpo de la solicitud
                    String body = buildNotificationBody(notification);
                    // Enviar la solicitud
                    OutputStream os = conn.getOutputStream();
                    os.write(body.getBytes());
                    os.flush();
                    // Verificar la respuesta
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // La notificación se envió correctamente
                        System.out.println("Notificación enviada correctamente.");
                        return new GenericResponse(TIPO_DATA, RPTA_OK, OPERACION_CORRECTA,
                                notification);
                    } else {
                        // Hubo un error al enviar la notificación
                        System.out.println("Error al enviar la notificación. Código de respuesta: " + responseCode);
                        return new GenericResponse(TIPO_DATA, RPTA_ERROR, OPERACION_ERRONEA,
                                "Error al enviar la notificación. Código de respuesta");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new GenericResponse(TIPO_DATA, RPTA_WARNING, OPERACION_ERRONEA,
                    "El deviceID no ha sido encontrado");
        }

        private static String buildNotificationBody(SendNotification notification) {
            return String.format(
                    "{\"to\":\"%s\",\"notification\":{\"title\":\"%s\",\"body\":\"%s\"}}",
                    notification.getId(), notification.getTitle(), notification.getMessage()
            );
        }
    }