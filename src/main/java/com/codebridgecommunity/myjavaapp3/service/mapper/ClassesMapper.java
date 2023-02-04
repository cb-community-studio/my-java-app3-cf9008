package com.codebridgecommunity.myjavaapp3.service.mapper;

import com.codebridgecommunity.myjavaapp3.domain.Classes;
import com.codebridgecommunity.myjavaapp3.service.dto.ClassesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Classes} and its DTO {@link ClassesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassesMapper extends EntityMapper<ClassesDTO, Classes> {}
