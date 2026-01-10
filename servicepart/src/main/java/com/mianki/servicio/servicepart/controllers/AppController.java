package com.mianki.servicio.servicepart.controllers;

import com.mianki.servicio.servicepart.models.dtos.MyNotesDto;
import com.mianki.servicio.servicepart.models.dtos.OptionRequest;
import com.mianki.servicio.servicepart.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping
    public ResponseEntity<?> findall() {
        return ResponseEntity.ok(appService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody MyNotesDto myNotesDto) {
        return ResponseEntity.status(201).body(appService.save(myNotesDto));
    }

    @PatchMapping
    public ResponseEntity<?> updatebyid(@RequestBody OptionRequest optionRequest) {
        return ResponseEntity.ok(appService.update(optionRequest));
    }

    @PatchMapping("/{id}/reset")
    public ResponseEntity<?> resetData(@PathVariable Long id) {
        appService.resetnote(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        appService.deletenote(id);
        return ResponseEntity.noContent().build();
    }
}
