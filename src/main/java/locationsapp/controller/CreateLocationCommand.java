package locationsapp.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CreateLocationCommand {

    @NotBlank
    private String name;

    @Coordinates
    private String coords;

    private LocalDateTime interestingAt;

    private String tags;

}
