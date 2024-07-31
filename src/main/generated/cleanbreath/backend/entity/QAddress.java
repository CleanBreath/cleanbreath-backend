package cleanbreath.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = -1559332088L;

    public static final QAddress address = new QAddress("address");

    public final StringPath addressCategory = createString("addressCategory");

    public final StringPath addressName = createString("addressName");

    public final NumberPath<Double> addressPosLat = createNumber("addressPosLat", Double.class);

    public final NumberPath<Double> addressPosLng = createNumber("addressPosLng", Double.class);

    public final StringPath buildingName = createString("buildingName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Path, QPath> paths = this.<Path, QPath>createList("paths", Path.class, QPath.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updateAt = createDateTime("updateAt", java.time.LocalDateTime.class);

    public QAddress(String variable) {
        super(Address.class, forVariable(variable));
    }

    public QAddress(com.querydsl.core.types.Path<? extends Address> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddress(PathMetadata metadata) {
        super(Address.class, metadata);
    }

}

