package ru.nikitapopov.weathermeasuresapi.utils;

public class SensorNotRegisterException extends RuntimeException{
    public SensorNotRegisterException(String message) {
        super(message);
    }
}
