package com.toilamanh.toilamanh.utils;

import com.toilamanh.toilamanh.dto.BookingDTO;
import com.toilamanh.toilamanh.dto.RoomDTO;
import com.toilamanh.toilamanh.dto.UserDTO;
import com.toilamanh.toilamanh.entity.Booking;
import com.toilamanh.toilamanh.entity.Room;
import com.toilamanh.toilamanh.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static final String ALPHANUMERIC_STRING="ADCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final SecureRandom secureRandom = new SecureRandom();
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            stringBuilder.append(ALPHANUMERIC_STRING.charAt(number));
        }
        return stringBuilder.toString();
    }
    public static UserDTO mapUserEntitytoUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());
        return roomDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        // map simple fields
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomDTO roomDTO = mapRoomEntityToRoomDTO(room);

        if (room.getBookings() != null) {
            roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }

        return roomDTO;
    }

   public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
       UserDTO userDTO = mapUserEntitytoUserDTO(user);
       if (!user.getBookings().isEmpty()) {
           userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRoom(booking, false)).collect(Collectors.toList()));
       }
       return userDTO;
   }

   public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRoom(Booking booking, boolean mapUser) {
       BookingDTO bookingDTO = new BookingDTO();
       // map simple fields
       bookingDTO.setId(booking.getId());
       bookingDTO.setCheckInDate(booking.getCheckInDate());
       bookingDTO.setCheckOutDate(booking.getCheckOutDate());
       bookingDTO.setNumOfAdults(booking.getNumOfAdults());
       bookingDTO.setNumOfChildren(booking.getNumOfChildren());
       bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
       bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
      if (mapUser) {
          bookingDTO.setUser(Utils.mapUserEntitytoUserDTO(booking.getUser()));
      }
      if (booking.getRoom() != null) {
          bookingDTO.setRoom(Utils.mapRoomEntityToRoomDTO(booking.getRoom()));
      }
      return bookingDTO;
   }

   public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntitytoUserDTO).collect(Collectors.toList());
   }

   public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList) {
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
   }
   public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
   }
}
