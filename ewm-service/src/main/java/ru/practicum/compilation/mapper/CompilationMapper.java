package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import ru.practicum.compilation.dto.CompilationResponse;
import ru.practicum.compilation.entity.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationResponse toCompilationResponse(Compilation compilation);

}
