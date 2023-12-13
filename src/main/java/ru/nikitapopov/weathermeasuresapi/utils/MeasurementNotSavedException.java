package ru.nikitapopov.weathermeasuresapi.utils;

public class MeasurementNotSavedException extends RuntimeException{
    public MeasurementNotSavedException(String message) {
        super(message);
    }
}
