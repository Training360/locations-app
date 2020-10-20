package locationsapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double lat;

    private double lon;

    private LocalDateTime interestingAt;

    @ElementCollection
    @CollectionTable(
            name="tag",
            joinColumns=@JoinColumn(name="location_id", referencedColumnName = "id")
    )
    @Column(name = "name")
    @OrderBy
    private List<String> tags;

}
