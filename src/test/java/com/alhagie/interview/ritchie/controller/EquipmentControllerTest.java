package com.alhagie.interview.ritchie.controller;

import com.alhagie.interview.ritchie.dto.EquipmentRequest;
import com.alhagie.interview.ritchie.entity.Equipment;
import com.alhagie.interview.ritchie.service.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipmentController.class)
class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EquipmentService equipmentService;

    private final static String BASE_URL = "/api/v1/equipments";

    @Test
    void save_equipment_should_return_no_content() throws Exception {
        // Given
        EquipmentRequest request = new EquipmentRequest("Excavator", 1, 1, 2023);
        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNoContent());

        verify(equipmentService, times(1)).saveEquipment(ArgumentMatchers.any());
    }

    @Test
    void save_equipment_should_return_throw_exception_missing_model_year() throws Exception {
        // Given
        EquipmentRequest request = new EquipmentRequest("Tractor", 1, 1, null);
        String requestJson = objectMapper.writeValueAsString(request);
        assertThrows(Exception.class, () -> mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)));
    }

    @Test
    void save_equipment_should_return_throw_exception_missing_hour_and_mileage() throws Exception {
        // Given
        EquipmentRequest request = new EquipmentRequest("Tractor", null, null, 2012);
        String requestJson = objectMapper.writeValueAsString(request);
        assertThrows(Exception.class, () -> mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)));
    }

    @Test
    void save_equipment_should_return_bad_request_when_type_is_blank() throws Exception {
        // Given
        EquipmentRequest request = new EquipmentRequest("", 1, 2, 2023);
        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void save_equipment_should_return_bad_request_when_type_is_null() throws Exception {
        // Given
        EquipmentRequest request = new EquipmentRequest(null, 1, 1, 2023);
        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_all_equipment_should_return_equipment_list() throws Exception {
        // Given
        List<Equipment> equipments = Arrays.asList(
                new Equipment(1, "Truck"),
                new Equipment(2, "Bool Dozer")
        );
        when(equipmentService.getAllEquipment()).thenReturn(equipments);

        // When & Then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].type", is("Truck")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].type", is("Bool Dozer")));

        verify(equipmentService).getAllEquipment();
    }

    @Test
    void get_equipment_by_id_should_return_equipment_when_found() throws Exception {
        // Given
        int id = 1;
        Equipment equipment = new Equipment(id, "Truck");
        when(equipmentService.getEquipmentById(id)).thenReturn(Optional.of(equipment));

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is("Truck")));

        verify(equipmentService).getEquipmentById(id);
    }

    @Test
    void get_equipment_by_id_should_return_not_found_when_equipment_does_not_exist() throws Exception {
        // Given
        int id = 99;
        when(equipmentService.getEquipmentById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(equipmentService).getEquipmentById(id);
    }

    @Test
    void delete_equipment_should_return_no_content() throws Exception {
        // Given
        int id = 1;

        // When & Then
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(equipmentService).deleteEquipment(id);
    }

    @Test
    void get_equipment_by_id_should_include_creation_time() throws Exception {
        // Given
        int id = 1;
        when(equipmentService.getEquipmentById(id)).thenReturn(Optional.of(mockEquipment()));

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creation_time").exists())
                .andExpect(jsonPath("$.creation_time").isNotEmpty());
    }

    private Equipment mockEquipment() {
        Equipment equipment = new Equipment(1, "Tractor");
        equipment.setCreation_time(LocalDateTime.of(2025, 1, 1, 1, 0, 0));
        return equipment;
    }
}