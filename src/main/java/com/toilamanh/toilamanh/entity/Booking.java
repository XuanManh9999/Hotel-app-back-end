package com.toilamanh.toilamanh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "check in date is required")
    private LocalDate checkInDate;
    @Future(message = "check out date must be in the future")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Number of adults must not be less that 1")
    private Integer numOfAdults;

    @Min(value = 0, message = "Number of children must not be less that 0")
    private Integer numOfChildren;

    private Integer totalNumOfGuest;

    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalNumOfGuest() {
        this.totalNumOfGuest = (this.numOfAdults != null ? this.numOfAdults : 0) +
                (this.numOfChildren != null ? this.numOfChildren : 0);
    }


    public void setNumOfChildren(@Min(value = 0, message = "Number of children must not be less than 0") Integer numOfChildren) {
        this.numOfChildren = (numOfChildren != null) ? numOfChildren : 0;
        calculateTotalNumOfGuest();
    }

    public void setNumOfAdults(@Min(value = 1, message = "Number of adults must not be less than 1") Integer numOfAdults) {
        this.numOfAdults = (numOfAdults != null) ? numOfAdults : 1;  // Đặt mặc định là 1 vì giá trị min là 1
        calculateTotalNumOfGuest();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numOfAdults=" + numOfAdults +
                ", numOfChildren=" + numOfChildren +
                ", totalNumOfGuest=" + totalNumOfGuest +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                '}';
    }
}
