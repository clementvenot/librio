package com.librio.backend.mappers;

import com.librio.backend.dto.book.BigListBookResponseDto;
import com.librio.backend.dto.book.CreateBookRequestDto;
import com.librio.backend.dto.book.LittleListBookResponseDto;
import com.librio.backend.entities.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    /* Création Entity (DTO -> Entity) */
    @Mapping(target = "id", ignore = true) // ignorer id (géré par la db)
    Book toEntity(CreateBookRequestDto dto);

    /* Création Dto (Entity -> DTO) */
    BigListBookResponseDto toBigListDto(Book entity);
    LittleListBookResponseDto toLittleListDto(Book entity);

    /* List entities */
    List<BigListBookResponseDto> toBigListDtos(List<Book> entities);
    List<LittleListBookResponseDto> toLittleListDtos(List<Book> entities);


    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) /* Ignore les champs null du DTO */
    void updateEntityFromCreateDto(CreateBookRequestDto dto, @MappingTarget Book entity); /* Update */
}
