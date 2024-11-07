package com.markian.rentitup.Category;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Machine.Machine;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "category")
public class Category  extends BaseEntity {

    @Column(nullable = false)
    private String name ;

    @OneToMany(mappedBy = "category" ,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Machine> machines;

    private String Description;

    private PriceCalculationType priceCalculationType;


}
