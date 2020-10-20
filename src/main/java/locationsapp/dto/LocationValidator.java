package locationsapp.dto;

import java.util.List;

public class LocationValidator {

    public void validate(String name, double lat, double lon, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add("Name can not be empty!");
        }if (lat < -90 || lat > 90) {
            errors.add("Latitude must be between -90 and 90!");
        }
        if (lon < -180 || lat > 180) {
            errors.add("Longitude must be between -180 and 180!");
        }
    }
}
