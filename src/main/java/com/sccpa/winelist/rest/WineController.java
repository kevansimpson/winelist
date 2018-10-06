package com.sccpa.winelist.rest;

import com.sccpa.winelist.data.WineEntry;
import com.sccpa.winelist.service.ImportDataService;
import com.sccpa.winelist.service.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WineController {

    private WineService wineService;
    private ImportDataService importDataService;

    @Autowired
    public WineController(final WineService bottleService, final ImportDataService importService) {
        wineService = bottleService;
        importDataService = importService;
    }


    @GetMapping("wine")
    public List<WineEntry> fetchEntireList() {
        return wineService.fetchEntireList();
    }

    @GetMapping("import")
    public Map<String, Object> importWines() {
        return importDataService.importCsvData();
    }

    @GetMapping("import/test")
    public Map<String, Object> testImportWines() {
        return importDataService.readCsvData();
    }
}
