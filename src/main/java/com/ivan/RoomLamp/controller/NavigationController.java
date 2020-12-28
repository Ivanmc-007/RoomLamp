package com.ivan.RoomLamp.controller;

import com.ivan.RoomLamp.exception.PageAccessException;
import com.ivan.RoomLamp.exception.RoomNotFoundException;
import com.ivan.RoomLamp.exception.UnknownIPAddressException;
import com.ivan.RoomLamp.service.GeolocationService;
import com.ivan.RoomLamp.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class NavigationController {

    private final static Logger LOG = LoggerFactory.getLogger(NavigationController.class);

    private final static String LOG_MESSAGE_CLIENT_IP = "Client IP: %s";

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

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
        LOG.info(String.format(LOG_MESSAGE_CLIENT_IP, clientIP));
        String countryClient = geolocationService.getCountryNameByIP(clientIP).orElseThrow(UnknownIPAddressException::new);

        if(nameCountryDB.equals(countryClient))
            return "one";
        throw new PageAccessException();
    }

    private String getClientIP(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (Objects.nonNull(ip) && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
