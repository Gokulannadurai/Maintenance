package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.LocationDTO;
import com.ideas2it.maintenanceservice.entity.Location;
import com.ideas2it.maintenanceservice.repository.LocationRepository;
import com.ideas2it.maintenanceservice.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @Mock LocationRepository locationRepository;
    @InjectMocks LocationServiceImpl locationService;

    private Location location;
    private LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setId(1L);
        location.setName("Conference Room");
        location.setDescription("Main conference room");

        locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setName("Conference Room");
        locationDTO.setDescription("Main conference room");
    }

    @Test
    void createLocation_success() {
        when(locationRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        LocationDTO result = locationService.createLocation(locationDTO);
        assertNotNull(result);
        assertEquals(location.getName(), result.getName());
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void createLocation_duplicateName_throws() {
        when(locationRepository.findByName(anyString())).thenReturn(Optional.of(location));
        assertThrows(IllegalArgumentException.class, () -> locationService.createLocation(locationDTO));
    }

    @Test
    void updateLocation_success() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        LocationDTO result = locationService.updateLocation(1L, locationDTO);
        assertNotNull(result);
        assertEquals(location.getName(), result.getName());
    }

    @Test
    void updateLocation_notFound_throws() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(1L, locationDTO));
    }

    @Test
    void deleteLocation_success() {
        when(locationRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(locationRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> locationService.deleteLocation(1L));
    }

    @Test
    void deleteLocation_notFound_throws() {
        when(locationRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> locationService.deleteLocation(1L));
    }

    @Test
    void getLocationById_found() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Optional<LocationDTO> result = locationService.getLocationById(1L);
        assertTrue(result.isPresent());
        assertEquals(location.getName(), result.get().getName());
    }

    @Test
    void getLocationById_notFound() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<LocationDTO> result = locationService.getLocationById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLocationByName_found() {
        when(locationRepository.findByName(anyString())).thenReturn(Optional.of(location));
        Optional<LocationDTO> result = locationService.getLocationByName("Conference Room");
        assertTrue(result.isPresent());
        assertEquals(location.getName(), result.get().getName());
    }

    @Test
    void getLocationByName_notFound() {
        when(locationRepository.findByName(anyString())).thenReturn(Optional.empty());
        Optional<LocationDTO> result = locationService.getLocationByName("Conference Room");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllLocations_success() {
        when(locationRepository.findAll()).thenReturn(List.of(location));
        List<LocationDTO> result = locationService.getAllLocations();
        assertEquals(1, result.size());
        assertEquals(location.getName(), result.get(0).getName());
    }
} 