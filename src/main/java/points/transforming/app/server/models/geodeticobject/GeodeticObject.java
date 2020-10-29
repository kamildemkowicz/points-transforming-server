package points.transforming.app.server.models.geodeticobject;

import javax.persistence.*;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GeodeticObject {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String description;
    private String symbol;
    private String color;
    private String measurementInternalId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "geodeticObject")
    private List<SingleLine> singleLines;
}
