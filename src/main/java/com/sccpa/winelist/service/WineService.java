package com.sccpa.winelist.service;

import com.sccpa.winelist.data.WineEntry;

import java.util.List;


public interface WineService {

    boolean login(String user, String pswd);

    List<WineEntry> fetchEntireList();

    WineEntry fetchEntry(long id);

    WineEntry insert(WineEntry entry);

    WineEntry update(WineEntry entry);
}
