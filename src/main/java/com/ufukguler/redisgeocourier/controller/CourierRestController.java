package com.ufukguler.redisgeocourier.controller;

import com.ufukguler.redisgeocourier.model.CourierLocation;
import com.ufukguler.redisgeocourier.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
@AllArgsConstructor
public class CourierRestController {
    private final CourierService courierService;

    @GetMapping("/locations")
    public List<CourierLocation> getCourierLocations() {
        return courierService.getAllCourierLocations();
    }
}
