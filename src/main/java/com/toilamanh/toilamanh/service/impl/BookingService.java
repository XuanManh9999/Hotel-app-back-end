package com.toilamanh.toilamanh.service.impl;

import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.entity.Booking;
import com.toilamanh.toilamanh.service.interfac.IBookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingService implements IBookingService {
    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        return null;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        return null;
    }

    @Override
    public Response getAllBookings() {
        return null;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        return null;
    }
}