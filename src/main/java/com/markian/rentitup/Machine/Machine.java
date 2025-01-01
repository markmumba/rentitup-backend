package com.markian.rentitup.Machine;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.Category.Category;
import com.markian.rentitup.MachineImage.MachineImage;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecord;
import com.markian.rentitup.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "machine")
public class Machine extends BaseEntity {
    private String name;

    private String description;

    private Boolean verified = false;

    private LocalDateTime verificationDeadline;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    private MachineAvailability status;

    @Enumerated(EnumType.STRING)
    private MachineCondition condition;

    @Column(length = 700)
    private String specification;

    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaintenanceRecord> maintainanceRecords;

    @OneToMany(mappedBy = "machine",fetch =FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MachineImage> machineImages;

    @Transient
    public boolean isVerificationValid() {
        return verified && verificationDeadline != null &&
                LocalDateTime.now().isBefore(verificationDeadline);
    }
}
