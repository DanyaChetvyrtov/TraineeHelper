package ru.core.profilems.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.core.profilems.validation.OnCreate;
import ru.core.profilems.validation.OnUpdate;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Profile data transfer object")
public class ProfileDto {
    @NotNull(message = "Id can't be null", groups = OnUpdate.class)
    @Schema(description = "Profile ID", example = "63ba3c9a-4c86-4a24-8e8a-5601edc55ef4", type = "UUID")
    private UUID profileId;

    @NotNull(message = "Name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 2, max = 100, message = "Name should be in range between 2 and 100 chars", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "Profile name", example = "Danil", type = "String")
    private String name;

    @NotNull(message = "Surname must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 2, max = 100, message = "Surname should be in range between 2 and 100 chars", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "Surname", example = "Chetvyrtov", type = "String")
    private String surname;

    @NotNull(message = "age must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 0, message = "Age can't be negative", groups = {OnCreate.class, OnUpdate.class})
    @Max(value = 100, message = "Age should be less or equal 100", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "Age", example = "25", type = "Integer")
    private Integer age;
}
