package com.ivan.RoomLamp.service;

import com.ivan.RoomLamp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
