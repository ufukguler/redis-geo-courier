package com.ufukguler.redisgeocourier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourierLocation {
    private String name;
    private double longitude;
    private double latitude;

}
