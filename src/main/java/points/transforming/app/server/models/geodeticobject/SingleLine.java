package points.transforming.app.server.models.geodeticobject;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SingleLine {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String startPicketInternalId;
    private String endPicketInternalId;

    @ManyToOne
    @JoinColumn(name = "geodetic_object_id")
    private GeodeticObject geodeticObject;
}
