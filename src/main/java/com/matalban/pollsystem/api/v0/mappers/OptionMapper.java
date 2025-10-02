package com.matalban.pollsystem.api.v0.mappers;

import com.matalban.pollsystem.api.v0.dto.OptionDto;
import com.matalban.pollsystem.domain.Option;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OptionMapper {


    public Option optionDtoToOption(OptionDto optionDto) {

        Option option = new Option();
        option.setId(optionDto.getId());
        option.setOptionName(optionDto.getMessage());
        option.setCreatedAt(new Date());
        return option;
    }

    public OptionDto optionToOptionDto(Option option) {

        OptionDto optionDto = new OptionDto();
        optionDto.setId(option.getId());
        optionDto.setMessage(option.getOptionName());
        optionDto.setCreatedAt(option.getCreatedAt());

        return optionDto;

    }

}
