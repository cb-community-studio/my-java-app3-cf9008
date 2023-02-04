package com.codebridgecommunity.myjavaapp3.service.mapper;

import com.codebridgecommunity.myjavaapp3.domain.Students;
import com.codebridgecommunity.myjavaapp3.service.dto.StudentsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Students} and its DTO {@link StudentsDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentsMapper extends EntityMapper<StudentsDTO, Students> {}
