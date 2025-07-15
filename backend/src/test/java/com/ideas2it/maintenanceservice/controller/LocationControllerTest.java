package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.LocationDTO;
import com.ideas2it.maintenanceservice.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.ideas2it.maintenanceservice.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {
    @InjectMocks LocationController locationController;
    @Mock LocationService locationService;
    private LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setName("Conference Room");
        locationDTO.setDescription("Main conference room");
    }

    @Test
    void createLocation_success() {
        when(locationService.createLocation(any(LocationDTO.class))).thenReturn(locationDTO);
        var response = locationController.createLocation(locationDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(locationDTO.getName(), response.getBody().getName());
    }

    @Test
    void createLocation_duplicateName_returnsBadRequest() {
        when(locationService.createLocation(any(LocationDTO.class))).thenThrow(new IllegalArgumentException("Location name already exists"));
        assertThrows(IllegalArgumentException.class, () -> locationController.createLocation(locationDTO));
    }

    @Test
    void updateLocation_success() {
        when(locationService.updateLocation(eq(1L), any(LocationDTO.class))).thenReturn(locationDTO);
        var response = locationController.updateLocation(1L, locationDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(locationDTO.getName(), response.getBody().getName());
    }

    @Test
    void updateLocation_notFound_returnsBadRequest() {
        when(locationService.updateLocation(eq(1L), any(LocationDTO.class))).thenThrow(new IllegalArgumentException("Location not found"));
        assertThrows(IllegalArgumentException.class, () -> locationController.updateLocation(1L, locationDTO));
    }

    @Test
    void deleteLocation_success() {
        doNothing().when(locationService).deleteLocation(1L);
        var response = locationController.deleteLocation(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteLocation_notFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("Location not found")).when(locationService).deleteLocation(1L);
        assertThrows(IllegalArgumentException.class, () -> locationController.deleteLocation(1L));
    }

    @Test
    void getLocationById_found() {
        when(locationService.getLocationById(1L)).thenReturn(Optional.of(locationDTO));
        var response = locationController.getLocationById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(locationDTO.getName(), response.getBody().getName());
    }

    @Test
    void getLocationById_notFound() {
        when(locationService.getLocationById(1L)).thenReturn(Optional.empty());
        var response = locationController.getLocationById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getLocationByName_found() {
        when(locationService.getLocationByName(anyString())).thenReturn(Optional.of(locationDTO));
        var response = locationController.getLocationByName("Conference Room");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(locationDTO.getName(), response.getBody().getName());
    }

    @Test
    void getLocationByName_notFound() {
        when(locationService.getLocationByName(anyString())).thenReturn(Optional.empty());
        var response = locationController.getLocationByName("Conference Room");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getAllLocations_success() {
        when(locationService.getAllLocations()).thenReturn(List.of(locationDTO));
        var response = locationController.getAllLocations();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(locationDTO.getName(), response.getBody().get(0).getName());
    }
} 