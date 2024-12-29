package com.markian.rentitup.Category;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Machine.Machine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Category  extends BaseEntity {

    @Column(nullable = false)
    private String name ;

    @OneToMany(mappedBy = "category" ,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Machine> machines;

    @Column(length = 700)
    private String description;

    private PriceCalculationType priceCalculationType;


}
