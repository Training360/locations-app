package locationsapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LocationDto {

    @Schema(description = "id of the location", example = "1")
    private Long id;

    @Schema(description="name of the location", example = "Budapest")
    private String name;

    @Schema(description="Latitude", example = "47.497912")
    private double lat;

    @Schema(description="Longitude", example = "19.040235")
    private double lon;

    @Schema(description="Interesting at", example = "2019-09-17T07:00:00")
    private LocalDateTime interestingAt;

    @Schema(description = "Tags", example = "capital,favourite")
    private List<String> tags;
}
