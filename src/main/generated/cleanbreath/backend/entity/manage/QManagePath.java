package cleanbreath.backend.entity.manage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QManagePath is a Querydsl query type for ManagePath
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManagePath extends EntityPathBase<ManagePath> {

    private static final long serialVersionUID = 1225821607L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QManagePath managePath = new QManagePath("managePath");

    public final EnumPath<cleanbreath.backend.entity.DivisionArea> divisionArea = createEnum("divisionArea", cleanbreath.backend.entity.DivisionArea.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QManageAddress manageAddress;

    public final StringPath pathLat = createString("pathLat");

    public final StringPath pathLng = createString("pathLng");

    public QManagePath(String variable) {
        this(ManagePath.class, forVariable(variable), INITS);
    }

    public QManagePath(Path<? extends ManagePath> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QManagePath(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QManagePath(PathMetadata metadata, PathInits inits) {
        this(ManagePath.class, metadata, inits);
    }

    public QManagePath(Class<? extends ManagePath> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.manageAddress = inits.isInitialized("manageAddress") ? new QManageAddress(forProperty("manageAddress")) : null;
    }

}

