package com.ivan.RoomLamp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivan.RoomLamp.entity.Room;
import com.ivan.RoomLamp.entity.View;
import com.ivan.RoomLamp.entity.dto.IdDto;
import com.ivan.RoomLamp.exception.RoomNotFoundException;
import com.ivan.RoomLamp.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

    private final RoomService roomService;

    public WebSocketController(RoomService roomService) {
        this.roomService = roomService;
    }

    @JsonView(View.IdNameCountry.class)
    @MessageMapping("/room")
    @SendTo("/topic/rooms")
    public Room greeting(Room room) {
        LOGGER.info("Creating room ... " + room);
        return roomService.save(room);
    }

    @JsonView(View.IdLamp.class)
    @MessageMapping("/roomLamp")
    @SendTo("/topic/roomOne")
    public Room clickOnLamp(IdDto id) {
        Room room = roomService.getById(id.getId()).orElseThrow(RoomNotFoundException::new);
        room.setLampOn(!room.isLampOn());
        return roomService.save(room);
    }

}
