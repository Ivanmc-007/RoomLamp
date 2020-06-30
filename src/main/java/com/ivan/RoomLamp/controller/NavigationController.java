package com.ivan.RoomLamp.controller;

import com.ivan.RoomLamp.exception.PageAccessException;
import com.ivan.RoomLamp.exception.RoomNotFoundException;
import com.ivan.RoomLamp.exception.UnknownIPAddressException;
import com.ivan.RoomLamp.service.GeolocationService;
import com.ivan.RoomLamp.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NavigationController {

    private final GeolocationService geolocationService;

    private final RoomService roomService;

    public NavigationController(GeolocationService geolocationService, RoomService roomService) {
        this.geolocationService = geolocationService;
        this.roomService = roomService;
    }

    @GetMapping("/roomWithLamp")
    public String one(HttpServletRequest request, @RequestParam(name = "roomId") Long id) {
        String nameCountryDB = roomService.getById(id).orElseThrow(RoomNotFoundException::new).getRoomCountry();

        String clientIP = getClientIP(request);
        String countryClient = geolocationService.getCountryNameByIP(clientIP).orElseThrow(UnknownIPAddressException::new);

        if(nameCountryDB.equals(countryClient))
            return "one.html";
        throw new PageAccessException();
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
