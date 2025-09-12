package com.matalban.pollsystem.api.v0.mappers;

import com.matalban.pollsystem.api.v0.dto.OptionDto;
import com.matalban.pollsystem.domain.Option;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OptionMapper {


    public Option optionDtoToOption(OptionDto optionDto) {

        Option option = new Option();
        option.setOptionName(optionDto.getMessage());
        option.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return option;
    }

    public OptionDto optionToOptionDto(Option option) {

        OptionDto optionDto = new OptionDto();
        optionDto.setId(optionDto.getId());
        optionDto.setMessage(option.getOptionName());
        optionDto.setCreatedAt(option.getCreatedAt());


        return optionDto;

    }

}
