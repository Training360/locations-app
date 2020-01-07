package locationsapp.ws;

import lombok.Data;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "lat", "lon", "interestingAt", "tags"})
public class CreateLocationRequest {

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private double lat;

    @XmlElement(required = true)
    private double lon;

    @XmlElement(name = "interesting-at")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime interestingAt;

    private String tags;

}
