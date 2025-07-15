package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.LocationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for location-related operations.
 */
public interface LocationService {
    LocationDTO createLocation(LocationDTO locationDTO);
    LocationDTO updateLocation(Long id, LocationDTO locationDTO);
    void deleteLocation(Long id);
    Optional<LocationDTO> getLocationById(Long id);
    Optional<LocationDTO> getLocationByName(String name);
    List<LocationDTO> getAllLocations();
} 