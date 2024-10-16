package com.toilamanh.toilamanh.service.impl;

import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.dto.RoomDTO;
import com.toilamanh.toilamanh.entity.Room;
import com.toilamanh.toilamanh.exception.OurException;
import com.toilamanh.toilamanh.repository.BookingRepository;
import com.toilamanh.toilamanh.repository.RoomRepository;
import com.toilamanh.toilamanh.service.interfac.IRoomService;
import com.toilamanh.toilamanh.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RoomService implements IRoomService {
    RoomRepository roomRepository;
    BookingRepository bookingRepository;
    @Override
    public Response addnewRoom(String photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();
        try {
            Room room = new Room();
            room.setRoomPhotoUrl(photo);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Room added successfully");
            response.setRoom(roomDTO);
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error saving room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOS = Utils.mapRoomListEntityToRoomListDTO(rooms);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room get successfully");
            response.setRoomList(roomDTOS);
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error getting rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room deleted successfully");
        }catch (OurException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Error delete room: " + e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error delete room: " + e.getMessage());
        }
        return response;
    }
    //roomId, roomType, roomPrice, photo, roomDescription
    @Override
    public Response updateRoom(Long roomId, String roomType, BigDecimal roomPrice, String photo, String description) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            if (roomType != null && !roomType.isEmpty()) {
                room.setRoomType(roomType);
            }
            if (roomPrice != null) {
                room.setRoomPrice(roomPrice);
            }
            if (photo != null && !photo.isEmpty()) {
                room.setRoomPhotoUrl(photo);
            }
            if (description != null && !description.isEmpty()) {
                room.setRoomDescription(description);
            }
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room updated successfully");
            response.setRoom(roomDTO);

        }catch (OurException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Error updated room: " + e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error updated room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room =  roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room get successfully");
            response.setRoom(roomDTO);

        }catch (OurException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Error get room by id: " + e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error get room by id: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();
        try {
            List<Room> availabeRooms = roomRepository.findAvailableRoomByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOS = Utils.mapRoomListEntityToRoomListDTO(availabeRooms);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room get successfully");
            response.setRoomList(roomDTOS);
        }
        catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error get room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOS = Utils.mapRoomListEntityToRoomListDTO(rooms);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room get successfully");
            response.setRoomList(roomDTOS);
        }catch (OurException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Error get room: " + e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error get room: " + e.getMessage());
        }
        return response;
    }
}
