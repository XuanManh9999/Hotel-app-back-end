package com.toilamanh.toilamanh.service.impl;

import com.toilamanh.toilamanh.dto.BookingDTO;
import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.entity.Booking;
import com.toilamanh.toilamanh.entity.Room;
import com.toilamanh.toilamanh.entity.User;
import com.toilamanh.toilamanh.exception.OurException;
import com.toilamanh.toilamanh.repository.BookingRepository;
import com.toilamanh.toilamanh.repository.RoomRepository;
import com.toilamanh.toilamanh.repository.UserRepository;
import com.toilamanh.toilamanh.service.interfac.IBookingService;
import com.toilamanh.toilamanh.service.interfac.IRoomService;
import com.toilamanh.toilamanh.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService implements IBookingService {

    BookingRepository bookingRepository;

    IRoomService roomService;

    RoomRepository roomRepository;

    UserRepository userRepository;


    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
      Response response = new Response();
      try {
          if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
              throw new IllegalArgumentException("Check in date must be after check out date");
          }
          Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
          User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User not found"));

          List<Booking> existingBookings = room.getBookings();
          if (!roomIsAvailable(bookingRequest, existingBookings)) {
            throw new OurException("Room is not available for this booking");
          }

          bookingRequest.setRoom(room);
          bookingRequest.setUser(user);

          String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
          bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
          bookingRepository.save(bookingRequest);

          response.setStatusCode(HttpStatus.OK.value());
          response.setMessage("Booking successful");

          response.setBookingConfirmationCode(bookingConfirmationCode);


      }catch (OurException e) {
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(e.getMessage());

      }catch (Exception e) {
          response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
          response.setMessage("Error Saving  booking" + e.getMessage());
      }
      return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                    || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                    || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()))
                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate())

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                            && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                            &&
                        bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())
                        )
                        )
                );
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTO(booking);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successful");
            response.setBooking(bookingDTO);

        }catch (OurException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error find  booking" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try {
            List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookings);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error finding a bookings" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking not found"));
            bookingRepository.delete(booking);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successful");

        }catch (OurException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error canceling a booking" + e.getMessage());
        }
        return response;
    }
}
