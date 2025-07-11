package com.alhagie.interview.ritchie.controller;

import com.alhagie.interview.ritchie.dto.EquipmentRequest;
import com.alhagie.interview.ritchie.entity.Equipment;
import com.alhagie.interview.ritchie.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<Void> saveEquipment(@RequestBody @Valid EquipmentRequest req) {
        String type = req.getType();
        if (type == null || type.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (req.getModelYear() == null || (req.getHour() == null && req.getMileage() == null)) {
            throw new RuntimeException("Invalid equipment request: Model year, hour, or mileage is missing.");
        }
        equipmentService.saveEquipment(req);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable int id) {
        return equipmentService.getEquipmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable int id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
