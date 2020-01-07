package locationsapp.controller;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LocationDto {

    private Long id;

    private String name;

    private double lat;

    private double lon;

    private LocalDateTime interestingAt;

    private List<String> tags;
}
