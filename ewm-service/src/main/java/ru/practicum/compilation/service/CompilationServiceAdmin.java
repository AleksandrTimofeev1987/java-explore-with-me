package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationCreate;
import ru.practicum.compilation.dto.CompilationResponse;
import ru.practicum.compilation.dto.CompilationUpdate;

public interface CompilationServiceAdmin {

    /**
     * Method adds compilation to repository.
     *
     * @param compilationDto Compilation to be added.
     *
     * @return Added compilation with assigned ID.
     */
    CompilationResponse createCompilation(CompilationCreate compilationDto);

    /**
     * Method deletes compilation from repository.
     *
     * @param compId ID of compilation to be deleted.
     */
    void deleteCompilation(Long compId);

    /**
     * Method updates compilation in repository.
     *
     * @param compId ID of compilation to be updated.
     * @param compilationDto Compilation update object.
     *
     * @return Updated compilation.
     */
    CompilationResponse updateCompilation(Long compId, CompilationUpdate compilationDto);
}
