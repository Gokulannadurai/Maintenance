package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Location entity.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    /**
     * Find a location by name.
     * @param name the location name
     * @return Optional of Location
     */
    Optional<Location> findByName(String name);
} 