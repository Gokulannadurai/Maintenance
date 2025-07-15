package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.LocationDTO;
import com.ideas2it.maintenanceservice.dto.mapper.LocationMapper;
import com.ideas2it.maintenanceservice.entity.Location;
import com.ideas2it.maintenanceservice.repository.LocationRepository;
import com.ideas2it.maintenanceservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of LocationService for location-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    /**
     * Create a new location in the system.
     * @param locationDTO the location data to create
     * @return the created LocationDTO
     * @throws IllegalArgumentException if location name already exists
     */
    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {
        log.info("Creating location with name: {}", locationDTO.getName());
        if (locationRepository.findByName(locationDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Location name already exists");
        }
        Location location = LocationMapper.toEntity(locationDTO);
        Location saved = locationRepository.save(location);
        return LocationMapper.toDTO(saved);
    }

    /**
     * Update an existing location's details.
     * @param id the location ID
     * @param locationDTO the updated location data
     * @return the updated LocationDTO
     * @throws IllegalArgumentException if location not found
     */
    @Override
    public LocationDTO updateLocation(Long id, LocationDTO locationDTO) {
        log.info("Updating location with id: {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        location.setName(locationDTO.getName());
        location.setDescription(locationDTO.getDescription());
        Location updated = locationRepository.save(location);
        return LocationMapper.toDTO(updated);
    }

    /**
     * Delete a location by ID.
     * @param id the location ID
     * @throws IllegalArgumentException if location not found
     */
    @Override
    public void deleteLocation(Long id) {
        log.info("Deleting location with id: {}", id);
        if (!locationRepository.existsById(id)) {
            throw new IllegalArgumentException("Location not found");
        }
        locationRepository.deleteById(id);
    }

    /**
     * Get a location by ID.
     * @param id the location ID
     * @return Optional of LocationDTO if found
     */
    @Override
    public Optional<LocationDTO> getLocationById(Long id) {
        log.info("Fetching location by id: {}", id);
        return locationRepository.findById(id).map(LocationMapper::toDTO);
    }

    /**
     * Get a location by name.
     * @param name the location name
     * @return Optional of LocationDTO if found
     */
    @Override
    public Optional<LocationDTO> getLocationByName(String name) {
        log.info("Fetching location by name: {}", name);
        return locationRepository.findByName(name).map(LocationMapper::toDTO);
    }

    /**
     * Get all locations in the system.
     * @return list of LocationDTOs
     */
    @Override
    public List<LocationDTO> getAllLocations() {
        log.info("Fetching all locations");
        return LocationMapper.toDTOs(locationRepository.findAll());
    }
} 