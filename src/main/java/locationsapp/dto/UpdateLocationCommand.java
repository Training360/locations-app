package locationsapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class UpdateLocationCommand {

    @Schema(description = "id of the location", example = "1")
    private long id;

    @NotBlank(message = "Name must not be blank")
    @Schema(description="name of the location", example = "Budapest")
    private String name;

    @Coordinates
    @Schema(description="Coordinates of the location", example = "47.497912,19.040235", required = true)
    private String coords;

    @Schema(description="Interesting at", example = "2019-09-17T07:00:00")
    private LocalDateTime interestingAt;

    @Schema(description = "Tags", example = "capital,favourite")
    private String tags;
}
