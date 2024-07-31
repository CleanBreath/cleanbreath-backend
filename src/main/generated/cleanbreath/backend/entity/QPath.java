package cleanbreath.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPath is a Querydsl query type for Path
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPath extends EntityPathBase<Path> {

    private static final long serialVersionUID = 1629224337L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPath path = new QPath("path");

    public final QAddress address;

    public final EnumPath<DivisionArea> divisionArea = createEnum("divisionArea", DivisionArea.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath pathLat = createString("pathLat");

    public final StringPath pathLng = createString("pathLng");

    public QPath(String variable) {
        this(Path.class, forVariable(variable), INITS);
    }

    public QPath(com.querydsl.core.types.Path<? extends Path> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPath(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPath(PathMetadata metadata, PathInits inits) {
        this(Path.class, metadata, inits);
    }

    public QPath(Class<? extends Path> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
    }

}

