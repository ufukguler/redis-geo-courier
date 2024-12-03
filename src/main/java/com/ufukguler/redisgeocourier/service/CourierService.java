package com.ufukguler.redisgeocourier.service;

import com.ufukguler.redisgeocourier.model.CourierLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourierService {
    private final StringRedisTemplate redisTemplate;
    private static final String REDIS_KEY = "couriers";
    private static final double BASE_LAT = 48.14;
    private static final double BASE_LON = 11.58;
    private final Random random = new Random();
    private static final String[] couriersArray = {"Courier1", "Courier2", "Courier3", "Courier4", "Courier5"};

    public CourierService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        initializeCouriers();
    }

    private void initializeCouriers() {
        for (int i = 1; i <= couriersArray.length; i++) {
            redisTemplate.opsForGeo().add(REDIS_KEY, new Point(BASE_LON, BASE_LAT), "Courier" + i);
        }
    }

    @Scheduled(fixedRate = 3_000)
    public void updateCourierLocations() {
        var courierNames = redisTemplate.opsForGeo().position(REDIS_KEY, couriersArray);
        if (CollectionUtils.isEmpty(courierNames)) {
            return;
        }

        for (int i = 0; i < courierNames.size(); i++) {
            if (courierNames.get(i) != null) {
                final String courierId = "Courier" + (i + 1);
                double newLat = courierNames.get(i).getY() + random.nextDouble() * 0.001 - 0.0008;
                double newLon = courierNames.get(i).getX() + random.nextDouble() * 0.001 - 0.0008;
                log.info("newLon:{}\tnewLat:{}\tid:{}", newLon, newLat, courierId);
                redisTemplate.opsForGeo().add(REDIS_KEY, new Point(newLon, newLat), courierId);
            }
        }
    }

    public List<CourierLocation> getAllCourierLocations() {
        var couriers = redisTemplate.opsForGeo().position(REDIS_KEY, couriersArray);
        if (CollectionUtils.isEmpty(couriers)) {
            return List.of();
        }
        return couriers
                .stream()
                .filter(Objects::nonNull)
                .map(pos -> new CourierLocation(
                        "Courier" + (couriers.indexOf(pos) + 1),
                        pos.getX(),
                        pos.getY()))
                .collect(Collectors.toList());
    }
}
