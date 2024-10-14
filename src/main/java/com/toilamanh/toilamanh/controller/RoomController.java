package com.toilamanh.toilamanh.controller;

import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.entity.Room;
import com.toilamanh.toilamanh.service.interfac.IBookingService;
import com.toilamanh.toilamanh.service.interfac.IRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {
    IRoomService roomService;
//    IBookingService bookingService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllRoomTypes() {
        List<String> roomTypes =  roomService.getAllRoomTypes();
        return ResponseEntity.status(HttpStatus.OK).body(roomTypes);
    }


    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId) {
        Response response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/room-available-rooms")
    public ResponseEntity<Response> getAllAvailableRooms() {
        Response response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomsByDateAndType (
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType
    ) {
        // check field
        if (checkInDate == null || checkOutDate == null || roomType == null || roomType.isBlank()) {
            Response response = new Response();
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Please provide all the required fields(checkInDate, checkOutDate, roomType)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.getAvailableRoomsByDataAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false) String photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription

    ) {
       if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null || roomDescription == null || roomDescription.isBlank()) {
            Response response = new Response();
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Please provide all the required fields(photo, roomType, roomPrice, roomDescription)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
       }
       Response response = roomService.addnewRoom(photo, roomType, roomPrice, roomDescription);
       return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(@PathVariable Long id,
                                               @RequestParam(value = "photo", required = false) String photo,
                                               @RequestParam(value = "roomType", required = false) String roomType,
                                               @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                               @RequestParam(value = "roomDescription", required = false) String roomDescription) {
        Response response = roomService.updateRoom(id, roomType, roomPrice, roomDescription, photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable(value = "roomId", required = false) Long roomId) {
        if (roomId == null || roomId <= 0) {
            Response response = new Response();
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Please provide a valid room id");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
