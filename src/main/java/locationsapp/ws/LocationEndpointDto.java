package locationsapp.ws;

import locationsapp.entities.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "lat", "lon", "interestingAt", "tags"})
public class LocationEndpointDto {

    @XmlAttribute
    private Long id;

    private String name;

    private double lat;

    private double lon;

    @XmlElement(name = "interesting-at")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime interestingAt;

    private String tags;

}
