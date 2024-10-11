package com.toilamanh.toilamanh.service.interfac;

import com.toilamanh.toilamanh.dto.LoginRequest;
import com.toilamanh.toilamanh.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    Response addnewRoom(String photo, String roomType, BigDecimal roomPrice, String description);
    List<String> getAllRoomTypes ();
    Response getAllRooms();
    Response deleteRoom(Long roomId);
    Response updateRoom(Long roomId, String roomType, BigDecimal roomPrice, String photo,  String description);
    Response getRoomById(Long roomId);
    Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    Response getAllAvailableRooms();
}
