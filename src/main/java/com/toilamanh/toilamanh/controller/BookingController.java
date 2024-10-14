package com.toilamanh.toilamanh.controller;

import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.entity.Booking;
import com.toilamanh.toilamanh.service.interfac.IBookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    IBookingService bookingService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByInformationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@PathVariable Long roomId,
                                                 @PathVariable Long userId,
                                                 @RequestBody Booking bookingRequest) {
        Response response = bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBookings(@PathVariable Long bookingId) {
        Response response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
