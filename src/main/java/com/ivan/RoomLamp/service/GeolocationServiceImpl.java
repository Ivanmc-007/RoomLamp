package com.ivan.RoomLamp.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Optional;

@Component
public class GeolocationServiceImpl implements GeolocationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GeolocationServiceImpl.class);

    private final DatabaseReader dbReader;

    @Value("${local.country.name}")
    private String localCountryName;

    public GeolocationServiceImpl() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/GeoLite2-Country/GeoLite2-Country.mmdb");
        InputStream database = classPathResource.getInputStream();
        dbReader = new DatabaseReader.Builder(database).build();
    }

    public Optional<String> getCountryNameByIP(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            if(isLocalhost(inetAddress)) {
                LOGGER.info("You are using this program on local machine!");
                return Optional.of(localCountryName);
            }
            CountryResponse countryResponse = dbReader.country(inetAddress);
            Country country = countryResponse.getCountry();
            return Optional.of(country.getName());
        } catch (UnknownHostException e) {
            LOGGER.error("Catch " + e);
            LOGGER.error("Local host name could not be resolved into an address");
        } catch (IOException | GeoIp2Exception e) {
            LOGGER.error("Catch " + e);
            LOGGER.error("There is an IO error or there is an error looking up the IP");
        }
        return Optional.empty();
    }

    private boolean isLocalhost(InetAddress inetAddress) {
        if(inetAddress.isSiteLocalAddress())
            return true;
        // Check if the address is a valid special local or loop back
        if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress())
            return true;
        // Check if the address is defined on any interface
        try {
            return NetworkInterface.getByInetAddress(inetAddress) != null;
        } catch (SocketException e) {
            return false;
        }
    }
}
