package com.sccpa.winelist.service;

import com.sccpa.winelist.data.WineEntry;
import com.sccpa.winelist.data.WineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WineService {
    private WineRepository wineRepository;

    @Autowired
    public WineService(final WineRepository repository) {
        wineRepository = repository;
    }


    public List<WineEntry> fetchEntireList() {
        return wineRepository.fetchEntireList();
    }
}
