package org.example.BlogEngine.service;

import org.example.BlogEngine.model.GlobalSettings;
import org.example.BlogEngine.repository.GlobalSettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SettingsService {

    private final GlobalSettingsRepository globalSettingsRepository;
    private GlobalSettings globalSettings;

    public SettingsService(GlobalSettingsRepository globalSettingsRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
    }


    public ResponseEntity<?> getApiSettings() {
        Map<String, Boolean> map = getBooleanMap();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private Map<String, Boolean> getBooleanMap() {
        Map<String, Boolean> map = new LinkedHashMap<>();
        globalSettings = globalSettingsRepository.findAll().stream().findFirst().orElse(new GlobalSettings());
        map.put("MULTIUSER_MODE", globalSettings.isMultiuserMode());
        map.put("POST_PREMODERATION", globalSettings.isPostPremoderation());
        map.put("STATISTICS_IS_PUBLIC", globalSettings.isStatisticsIsPublic());
        return map;
    }
}

