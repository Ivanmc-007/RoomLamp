package com.ivan.RoomLamp.service;

import java.util.Optional;

public interface GeolocationService {

    Optional<String> getCountryNameByIP(String ipAddress);

}
