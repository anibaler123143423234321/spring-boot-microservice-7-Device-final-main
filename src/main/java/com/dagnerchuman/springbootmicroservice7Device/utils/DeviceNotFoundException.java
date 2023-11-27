package com.dagnerchuman.springbootmicroservice7Device.utils;

public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException(String message) {
        super(message);
    }
}
