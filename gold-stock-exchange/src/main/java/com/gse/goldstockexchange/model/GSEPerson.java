package com.gse.goldstockexchange.model;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class GSEPerson {

    private String name;
    private LocalTime timestamp;
    private int rate;
    private int quantity;

}
