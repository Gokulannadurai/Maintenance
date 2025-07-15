package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.LocationDTO;
import com.ideas2it.maintenanceservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for location-related operations.
 */
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;

    /**
     * Create a new location.
     * @param locationDTO the location data
     * @return the created location
     */
    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDTO) {
        log.info("API: Creating location with name: {}", locationDTO.getName());
        LocationDTO created = locationService.createLocation(locationDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing location.
     * @param id the location ID
     * @param locationDTO the updated location data
     * @return the updated location
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationDTO locationDTO) {
        log.info("API: Updating location with id: {}", id);
        LocationDTO updated = locationService.updateLocation(id, locationDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a location by ID.
     * @param id the location ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.info("API: Deleting location with id: {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a location by ID.
     * @param id the location ID
     * @return the location if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        log.info("API: Fetching location by id: {}", id);
        Optional<LocationDTO> location = locationService.getLocationById(id);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get a location by name.
     * @param name the location name
     * @return the location if found
     */
    @GetMapping("/by-name")
    public ResponseEntity<LocationDTO> getLocationByName(@RequestParam String name) {
        log.info("API: Fetching location by name: {}", name);
        Optional<LocationDTO> location = locationService.getLocationByName(name);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all locations.
     * @return list of locations
     */
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        log.info("API: Fetching all locations");
        return ResponseEntity.ok(locationService.getAllLocations());
    }
} 