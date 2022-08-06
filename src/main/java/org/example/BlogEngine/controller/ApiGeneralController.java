package org.example.BlogEngine.controller;

import org.example.BlogEngine.response.InitResponse;
import org.example.BlogEngine.service.CalendarService;
import org.example.BlogEngine.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// для прочих запросов к API
@RestController
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final CalendarService calendarService;

    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse, CalendarService calendarService) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
        this.calendarService = calendarService;
    }

    @GetMapping("/api/init")
    private InitResponse init(){
        return initResponse;
    }

    @GetMapping("/api/settings")
    private ResponseEntity<?> getApiSettings () {
        return settingsService.getApiSettings();
    }

    @GetMapping("/api/calendar")
    private ResponseEntity <?> getApiCalendar (@RequestParam Integer year) {
        return calendarService.getApiCalendar (year);
    }
}
