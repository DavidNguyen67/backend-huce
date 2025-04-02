package com.tigeren.backend.dto;

import com.tigeren.backend.validator.AllStringAndNotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeleteRecordDTO {
    @NotNull(message = "{ids_required}")
    @AllStringAndNotBlank
    public List<String> ids;
}
