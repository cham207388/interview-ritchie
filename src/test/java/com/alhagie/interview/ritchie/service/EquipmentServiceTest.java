package com.alhagie.interview.ritchie.service;

import com.alhagie.interview.ritchie.dto.EquipmentRequest;
import com.alhagie.interview.ritchie.entity.Equipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentServiceTest {

    private EquipmentService equipmentService;

    @BeforeEach
    void setUp() {
        // Clear the static list before each test
        EquipmentService.EQUIPMENTS.clear();
        EquipmentService.EQUIPMENTS.addAll(EquipmentService.getEquipmentList());
        equipmentService = new EquipmentService();
    }

    @Test
    void getAllEquipment_shouldReturnAllEquipments() {
        // When
        List<Equipment> result = equipmentService.getAllEquipment();

        // Then
        assertEquals(4, result.size());
        assertEquals("Truck", result.get(0).getType());
        assertEquals("Bool Dozer", result.get(1).getType());
    }

    @Test
    void saveEquipment_shouldAddNewEquipmentWithIncrementedId() {
        // Given
        EquipmentRequest request = new EquipmentRequest("Excavator");
        int initialSize = equipmentService.getAllEquipment().size();

        // When
        equipmentService.saveEquipment(request);

        // Then
        List<Equipment> equipments = equipmentService.getAllEquipment();
        assertEquals(initialSize + 1, equipments.size());

        Equipment newEquipment = equipments.get(equipments.size() - 1);
        assertEquals(5, newEquipment.getId()); // Since max ID was 4
        assertEquals("Excavator", newEquipment.getType());
    }

    @Test
    void deleteEquipment_shouldRemoveEquipmentWithGivenId() {
        // Given
        int idToDelete = 2;
        int initialSize = equipmentService.getAllEquipment().size();

        // When
        equipmentService.deleteEquipment(idToDelete);

        // Then
        List<Equipment> equipments = equipmentService.getAllEquipment();
        assertEquals(initialSize - 1, equipments.size());
        assertTrue(equipments.stream().noneMatch(e -> e.getId() == idToDelete));
    }

    @Test
    void deleteEquipment_whenIdNotFound_shouldNotChangeList() {
        // Given
        int nonExistentId = 99;
        int initialSize = equipmentService.getAllEquipment().size();

        // When
        equipmentService.deleteEquipment(nonExistentId);

        // Then
        assertEquals(initialSize, equipmentService.getAllEquipment().size());
    }

    @Test
    void getEquipmentById_shouldReturnEquipmentWhenFound() {
        // Given
        int existingId = 3;

        // When
        Optional<Equipment> result = equipmentService.getEquipmentById(existingId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Lift", result.get().getType());
    }

    @Test
    void getEquipmentById_shouldReturnEmptyWhenNotFound() {
        // Given
        int nonExistentId = 99;

        // When
        Optional<Equipment> result = equipmentService.getEquipmentById(nonExistentId);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void generateId_shouldReturnMaxIdFromExistingEquipments() {
        // When
        int generatedId = equipmentService.generateId();

        // Then
        assertEquals(4, generatedId);
    }

    @Test
    void generateId_whenNoEquipments_shouldReturnZero() {
        // Given
        EquipmentService.EQUIPMENTS.clear();

        // When
        int generatedId = equipmentService.generateId();

        // Then
        assertEquals(0, generatedId);
    }
}