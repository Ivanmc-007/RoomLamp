package com.ivan.RoomLamp.service;

import com.ivan.RoomLamp.entity.Room;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<Room> getById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }

}
