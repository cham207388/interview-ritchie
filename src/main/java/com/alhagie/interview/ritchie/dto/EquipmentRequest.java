package com.alhagie.interview.ritchie.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentRequest {
    @NotEmpty
    private String type;
    private Integer hour;
    private Integer mileage;
    private Integer modelYear;
}
