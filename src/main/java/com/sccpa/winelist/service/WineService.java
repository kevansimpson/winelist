package com.sccpa.winelist.service;

import com.sccpa.winelist.data.WineEntry;

import java.util.List;


public interface WineService {

    boolean isLoggedIn();

    boolean login(String user, String pswd);

    void logout();

    List<WineEntry> fetchEntireList();

    WineEntry fetchEntry(long id);

    WineEntry insert(WineEntry entry);

    WineEntry update(WineEntry entry);

    WineEntry delete(WineEntry entry);
}
