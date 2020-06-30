package com.ivan.RoomLamp.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GeolocationServiceImpl implements GeolocationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GeolocationServiceImpl.class);

    private final DatabaseReader dbReader;

    @Value("${local.country.name}")
    private String localCountryName;

    private List<String> localIpAddresses = new ArrayList<String>() {
        {
            this.add("0:0:0:0:0:0:0:1");
            this.add("127.0.0.1");
        }
    };

    public GeolocationServiceImpl() throws IOException {
        File database = ResourceUtils.getFile("classpath:static/GeoLite2-Country/GeoLite2-Country.mmdb");
        dbReader = new DatabaseReader.Builder(database).build();
    }

    public Optional<String> getCountryNameByIP(String ipAddress) {
        try {
            if(isLocalhost(ipAddress)) {
                LOGGER.info("You use this program on local machine!");
                return Optional.of(localCountryName);
            }
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
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

    private boolean isLocalhost(String ipAddress) {
        for(String address: localIpAddresses) {
            if(address.equals(ipAddress))
                return true;
        }
        return false;
    }
}
