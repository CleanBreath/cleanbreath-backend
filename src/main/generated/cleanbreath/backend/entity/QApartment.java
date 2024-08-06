package cleanbreath.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApartment is a Querydsl query type for Apartment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApartment extends EntityPathBase<Apartment> {

    private static final long serialVersionUID = 1510457670L;

    public static final QApartment apartment = new QApartment("apartment");

    public final StringPath address = createString("address");

    public final StringPath apartmentName = createString("apartmentName");

    public final ListPath<ApartmentPath, QApartmentPath> apartmentPaths = this.<ApartmentPath, QApartmentPath>createList("apartmentPaths", ApartmentPath.class, QApartmentPath.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> designationDate = createDate("designationDate", java.time.LocalDate.class);

    public final StringPath designationNumber = createString("designationNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> numberOfBuilding = createNumber("numberOfBuilding", Integer.class);

    public final NumberPath<Integer> numberOfHouseholds = createNumber("numberOfHouseholds", Integer.class);

    public final StringPath region = createString("region");

    public QApartment(String variable) {
        super(Apartment.class, forVariable(variable));
    }

    public QApartment(Path<? extends Apartment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApartment(PathMetadata metadata) {
        super(Apartment.class, metadata);
    }

}

