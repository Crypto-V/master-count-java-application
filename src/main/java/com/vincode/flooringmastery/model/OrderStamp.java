package com.vincode.flooringmastery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderStamp {
    private String date;
    private Integer orderNumber;
    private Order orderDetail;

}
