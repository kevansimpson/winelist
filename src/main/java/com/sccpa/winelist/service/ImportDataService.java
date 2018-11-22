package com.sccpa.winelist.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sccpa.winelist.data.WineEntry;
import com.sccpa.winelist.data.WineRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImportDataService {
    @Value("${data.file:/jmswine.csv}")
    private String dataFile;

    private WineRepository wineRepository;

    @Autowired
    public ImportDataService(final WineRepository repository) {
        wineRepository = repository;
    }

    public Map<String, Object> importCsvData() {
        final Map<String, Object> csvData = readCsvData();
        final List<WineEntry> data = (List<WineEntry>) csvData.get("data");
        data.forEach(wineRepository::insert);

        return csvData;
    }

    public Map<String, Object> readCsvData() {
        final Map<String, Object> result = new LinkedHashMap<>();
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(dataFile));
             // first line is the header, which we skip with config below ---v
             CSVReader csvReader = (new CSVReaderBuilder(reader)).withSkipLines(1).build()) {

            final List<WineEntry> data = new ArrayList<>();
            final int[] maxLengths = new int[10];

            csvReader.forEach(strings -> {
                final WineEntry entry = new WineEntry();
                // "BottleID","Producer","Name","Type","Year","Price","Qty","Bin","Ready","Rating"
                entry.setProducer(strings[1]);
                entry.setName(strings[2]);
                entry.setType(strings[3]);
                entry.setYear(strings[4]);
                entry.setPrice(NumberUtils.toDouble(strings[5]));
                entry.setQty(NumberUtils.toInt(strings[6]));
                entry.setBin(strings[7]);
                entry.setReady(strings[8]);
                entry.setRating(strings[9]);
                data.add(entry);

                for (int i = 1; i < 10; i++) {
                    maxLengths[i] = Math.max(maxLengths[i], strings[i].length());
                }
            });

            result.put("lineCount", csvReader.getLinesRead());
            result.put("recordCount", csvReader.getRecordsRead());
            result.put("maxLengths", maxLengths);
            result.put("data", data);
        } catch (Exception ex) {
            result.put("error", ex.getMessage());
        }

        return result;
    }
}
