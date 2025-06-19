package com.alhagie.interview.ritchie.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Equipment {
    private int id;
    private String type;
    private LocalDateTime creation_time;
    private Integer modelYear;
    private Integer hour;
    private Integer mileage;

    public Equipment(int id, String type) {
        this.id = id;
        this.type = type;
        this.creation_time = LocalDateTime.now();
    }
}
