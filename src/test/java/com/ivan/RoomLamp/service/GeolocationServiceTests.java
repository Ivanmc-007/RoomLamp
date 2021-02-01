package com.ivan.RoomLamp.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class GeolocationServiceTests {

	private final static String BELARUS_IP = "37.214.33.201";
	private final static String BELARUS_IP_2 = "93.125.107.90";
	private final static String RUSSIA_IP = "195.208.131.1";
	private final static String UKRAINE_IP = "185.121.110.115";
	private final static String Local_IP_1 = "172.17.0.1";
	private final static String Local_IP_2 = "192.168.1.2";
    private final static String Local_IP_3 = "127.0.0.1";
	private final static String Local_IP_4 = "0:0:0:0:0:0:0:1";

	@Value("${local.country.name}")
	private String localCountry;

	@Autowired
	private GeolocationService geolocationService;

	@Test
	public void contextLoads() {
		Assert.assertNotNull(geolocationService);
	}

	@Test
	public void checkCountryByIP() {
		Assert.assertEquals(geolocationService.getCountryNameByIP(BELARUS_IP).orElse(""),"Belarus");
		Assert.assertEquals(geolocationService.getCountryNameByIP(BELARUS_IP_2).orElse(""),"Belarus");
		Assert.assertEquals(geolocationService.getCountryNameByIP(RUSSIA_IP).orElse(""),"Russia");
		Assert.assertEquals(geolocationService.getCountryNameByIP(UKRAINE_IP).orElse(""),"Ukraine");
	}

	@Test
	public void checkCountryByLocalIP() {
		Assert.assertEquals(geolocationService.getCountryNameByIP(Local_IP_1).orElse(""), localCountry);
		Assert.assertEquals(geolocationService.getCountryNameByIP(Local_IP_2).orElse(""), localCountry);
        Assert.assertEquals(geolocationService.getCountryNameByIP(Local_IP_3).orElse(""), localCountry);
		Assert.assertEquals(geolocationService.getCountryNameByIP(Local_IP_4).orElse(""), localCountry);
	}

}
