package locationsapp.ws;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v, DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        return DateTimeFormatter.ISO_DATE_TIME.format(v);
    }
}
