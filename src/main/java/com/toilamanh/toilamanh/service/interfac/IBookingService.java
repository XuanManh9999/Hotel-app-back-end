package com.toilamanh.toilamanh.service.interfac;

import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.entity.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode (String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId) ;

}
