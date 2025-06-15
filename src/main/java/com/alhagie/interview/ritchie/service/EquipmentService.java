package com.alhagie.interview.ritchie.service;

import com.alhagie.interview.ritchie.dto.EquipmentRequest;
import com.alhagie.interview.ritchie.entity.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    public static final List<Equipment> EQUIPMENTS = getEquipmentList();
    
    public void saveEquipment(EquipmentRequest request) {
        Equipment equipment = new Equipment(generateId() + 1, request.getType());
        EQUIPMENTS.add(equipment);
    }
    
    public List<Equipment> getAllEquipment() {
        return EQUIPMENTS;
    }
    
    public void deleteEquipment(int id) {
        EQUIPMENTS.removeIf(equipment -> equipment.getId() == id);
    }
    
    public Optional<Equipment> getEquipmentById(int id) {
        return EQUIPMENTS.stream()
                .filter(equipment -> equipment.getId() == id)
                .findFirst();
    }

    static List<Equipment> getEquipmentList() {

        var equipments = new ArrayList<Equipment>();
        equipments.add(new Equipment(1, "Truck"));
        equipments.add(new Equipment(2, "Bool Dozer"));
        equipments.add(new Equipment(3, "Lift"));
        equipments.add(new Equipment(4, "Scale"));
        return equipments;
    }
    
    int generateId() {
        return EQUIPMENTS.stream()
                .mapToInt(Equipment::getId)
                .max()
                .orElse(0);
    }
}