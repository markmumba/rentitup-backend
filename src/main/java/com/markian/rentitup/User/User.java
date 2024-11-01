package com.markian.rentitup.User;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Review.Review;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "user_table")

public class User  extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    private  String password;

    private String fullName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate verifiedAt;

    @OneToMany(mappedBy = "owner")
    private List<Machine> ownedMachines;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviews;

}
