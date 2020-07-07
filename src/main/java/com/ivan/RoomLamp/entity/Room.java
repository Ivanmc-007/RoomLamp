package com.ivan.RoomLamp.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Room {

    @JsonView({
            View.IdNameCountry.class,
            View.IdNameCountryLamp.class,
            View.IdLamp.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView({
            View.IdNameCountry.class,
            View.IdNameCountryLamp.class})
    private String roomName;

    @JsonView({
            View.IdNameCountry.class,
            View.IdNameCountryLamp.class})
    private String roomCountry;

    @JsonView({
            View.IdNameCountryLamp.class,
            View.IdLamp.class})
    private boolean lampOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomCountry() {
        return roomCountry;
    }

    public void setRoomCountry(String roomCountry) {
        this.roomCountry = roomCountry;
    }

    public boolean isLampOn() {
        return lampOn;
    }

    public void setLampOn(boolean lampOn) {
        this.lampOn = lampOn;
    }

}
