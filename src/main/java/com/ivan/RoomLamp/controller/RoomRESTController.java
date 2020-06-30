package com.ivan.RoomLamp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivan.RoomLamp.entity.Room;
import com.ivan.RoomLamp.entity.View;
import com.ivan.RoomLamp.exception.RoomNotFoundException;
import com.ivan.RoomLamp.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("room")
public class RoomRESTController {
    private final RoomService roomService;

    public RoomRESTController(RoomService roomService) {
        this.roomService = roomService;
    }

    @JsonView(View.IdNameCountry.class)
    @GetMapping
    public List<Room> getAll() {
        return roomService.findAll();
    }

    @JsonView(View.IdNameCountryLamp.class)
    @GetMapping("{id}")
    public Room getById(@PathVariable Long id) {
        return roomService.getById(id).orElseThrow(RoomNotFoundException::new);
    }

    @PostMapping
    public Room save(@RequestBody Room room) {
        return roomService.save(room);
    }

}
