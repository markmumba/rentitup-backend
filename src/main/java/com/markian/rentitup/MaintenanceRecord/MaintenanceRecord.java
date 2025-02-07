package com.markian.rentitup.MaintenanceRecord;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Machine.Machine;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class MaintenanceRecord extends BaseEntity {

    private LocalDate serviceDate;

    private String description;

    private String performedBy;


    @Column(nullable = false)
    private Boolean checked = Boolean.FALSE ;

    private LocalDate nextService;

    private String imageRecordUrl;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
