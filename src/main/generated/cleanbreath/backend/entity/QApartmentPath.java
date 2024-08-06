package cleanbreath.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApartmentPath is a Querydsl query type for ApartmentPath
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApartmentPath extends EntityPathBase<ApartmentPath> {

    private static final long serialVersionUID = -1572895093L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApartmentPath apartmentPath = new QApartmentPath("apartmentPath");

    public final QApartment apartment;

    public final NumberPath<Double> apartmentLat = createNumber("apartmentLat", Double.class);

    public final NumberPath<Double> apartmentLng = createNumber("apartmentLng", Double.class);

    public final EnumPath<NonSmokingStatus> elevator = createEnum("elevator", NonSmokingStatus.class);

    public final EnumPath<NonSmokingStatus> hallway = createEnum("hallway", NonSmokingStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<NonSmokingStatus> stairs = createEnum("stairs", NonSmokingStatus.class);

    public final EnumPath<NonSmokingStatus> undergroundParkingLot = createEnum("undergroundParkingLot", NonSmokingStatus.class);

    public QApartmentPath(String variable) {
        this(ApartmentPath.class, forVariable(variable), INITS);
    }

    public QApartmentPath(Path<? extends ApartmentPath> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApartmentPath(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApartmentPath(PathMetadata metadata, PathInits inits) {
        this(ApartmentPath.class, metadata, inits);
    }

    public QApartmentPath(Class<? extends ApartmentPath> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.apartment = inits.isInitialized("apartment") ? new QApartment(forProperty("apartment")) : null;
    }

}

