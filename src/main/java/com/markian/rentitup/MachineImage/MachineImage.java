package com.markian.rentitup.MachineImage;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Machine.Machine;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class MachineImage extends BaseEntity {
    private String url;
    private Boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
