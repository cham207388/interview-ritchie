package com.alhagie.interview.ritchie.controller;

import com.alhagie.interview.ritchie.dto.EquipmentRequest;
import com.alhagie.interview.ritchie.entity.Equipment;
import com.alhagie.interview.ritchie.service.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
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

//    @Test
//    void save_equipment_should_return_no_content() throws Exception {
//        // Given
//        EquipmentRequest request = new EquipmentRequest("Excavator");
//        String requestJson = objectMapper.writeValueAsString(request);
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/equipment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(equipmentService).saveEquipment(request);
//    }

    @Test
    void get_all_equipment_should_return_equipment_list() throws Exception {
        // Given
        List<Equipment> equipments = Arrays.asList(
                new Equipment(1, "Truck"),
                new Equipment(2, "Bool Dozer")
        );
        Mockito.when(equipmentService.getAllEquipment()).thenReturn(equipments);

        // When & Then
        mockMvc.perform(get("/api/v1/equipment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].type", is("Truck")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].type", is("Bool Dozer")));

        Mockito.verify(equipmentService).getAllEquipment();
    }

    @Test
    void get_equipment_by_id_should_return_equipment_when_found() throws Exception {
        // Given
        int id = 1;
        Equipment equipment = new Equipment(id, "Truck");
        Mockito.when(equipmentService.getEquipmentById(id)).thenReturn(Optional.of(equipment));

        // When & Then
        mockMvc.perform(get("/api/v1/equipment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is("Truck")));

        Mockito.verify(equipmentService).getEquipmentById(id);
    }

    @Test
    void get_equipment_by_id_should_return_not_found_when_equipment_does_not_exist() throws Exception {
        // Given
        int id = 99;
        Mockito.when(equipmentService.getEquipmentById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/equipment/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(equipmentService).getEquipmentById(id);
    }

    @Test
    void delete_equipment_should_return_no_content() throws Exception {
        // Given
        int id = 1;

        // When & Then
        mockMvc.perform(delete("/api/v1/equipment/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(equipmentService).deleteEquipment(id);
    }

    @Test
    void save_equipment_should_return_bad_request_when_type_is_blank() throws Exception {
        // Given
        EquipmentRequest request = new EquipmentRequest("");
        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/v1/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void save_equipment_should_return_bad_request_when_type_is_null() throws Exception {
        // Given
        EquipmentRequest request = new EquipmentRequest(null);
        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/v1/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}