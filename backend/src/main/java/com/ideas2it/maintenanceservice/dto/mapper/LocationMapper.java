package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.LocationDTO;
import com.ideas2it.maintenanceservice.entity.Location;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for Location and LocationDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class LocationMapper {
    /**
     * Convert Location entity to LocationDTO.
     */
    public static LocationDTO toDTO(Location location) {
        if (location == null) return null;
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setDescription(location.getDescription());
        return dto;
    }

    /**
     * Convert LocationDTO to Location entity.
     */
    public static Location toEntity(LocationDTO dto) {
        if (dto == null) return null;
        Location location = new Location();
        location.setId(dto.getId());
        location.setName(dto.getName());
        location.setDescription(dto.getDescription());
        return location;
    }

    /**
     * Convert a list of Location entities to a list of LocationDTOs.
     */
    public static List<LocationDTO> toDTOs(List<Location> locations) {
        return locations == null ? null : locations.stream().map(LocationMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of LocationDTOs to a list of Location entities.
     */
    public static List<Location> toEntities(List<LocationDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(LocationMapper::toEntity).collect(Collectors.toList());
    }
} 