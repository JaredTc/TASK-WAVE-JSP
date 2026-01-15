package com.taskwave.taskwave.util;

import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.entity.Tasks;
import org.mapstruct.*;

/**
 * Mapper encargado de transformar objetos relacionados con la entidad Tasks.
 *
 * Responsabilidades:
 * - Actualizar una entidad Tasks desde un DTO de entrada (PATCH)
 * - Convertir la entidad Tasks a un DTO de respuesta
 *
 * Reglas importantes:
 * - No se mapean relaciones JPA directamente (User, Category)
 * - Los valores null NO sobrescriben datos existentes
 *
 * MapStruct se utiliza para evitar código repetitivo y errores manuales.
 */
@Mapper(componentModel = "spring")
public interface TasksMapper {

    /**
     * Actualiza una entidad Tasks a partir de un TasksDTO.
     *
     * - Solo se actualizan los campos que NO sean null en el DTO.
     * - Las relaciones JPA (assignedTo, category, createdBy) se ignoran,
     *   ya que deben resolverse manualmente en la capa de servicio.
     *
     * @param dto    DTO con los datos a actualizar
     * @param entity Entidad Tasks persistida
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "assignedTo", ignore = true),
            @Mapping(target = "createdBy", ignore = true)
    })
    void updateTaskFromDto(TasksDTO dto, @MappingTarget Tasks entity);
    /**
     * Convierte una entidad Tasks en un DTO de respuesta.
     *
     * - Extrae valores simples desde relaciones JPA:
     *   User      → username
     *   Category  → name
     *
     * @param task Entidad Tasks
     * @return DTO listo para ser enviado al cliente
     */
    @Mappings({
            @Mapping(target = "assignedTo", source = "assignedTo.username"),
            @Mapping(target = "category", source = "category.name"),
            @Mapping(target = "createdBy", source = "createdBy.username")
    })
    TasksResDTO toResponseDto(Tasks task);
}
