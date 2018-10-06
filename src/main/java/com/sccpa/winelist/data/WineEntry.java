package com.sccpa.winelist.data;

import lombok.Data;

@Data
public class WineEntry {
    private long id;
    private String producer;
    private String name;
    private String type;
    private String year;
    private String price;
    private String qty;
    private String bin;
    private String ready;
    private String rating;
}
