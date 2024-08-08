package cleanbreath.backend.entity.manage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSmokingAreaValidate is a Querydsl query type for SmokingAreaValidate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSmokingAreaValidate extends EntityPathBase<SmokingAreaValidate> {

    private static final long serialVersionUID = -740822158L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSmokingAreaValidate smokingAreaValidate = new QSmokingAreaValidate("smokingAreaValidate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QManageAddress manageAddress;

    public final NumberPath<Integer> truth = createNumber("truth", Integer.class);

    public final NumberPath<Integer> untruth = createNumber("untruth", Integer.class);

    public QSmokingAreaValidate(String variable) {
        this(SmokingAreaValidate.class, forVariable(variable), INITS);
    }

    public QSmokingAreaValidate(Path<? extends SmokingAreaValidate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSmokingAreaValidate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSmokingAreaValidate(PathMetadata metadata, PathInits inits) {
        this(SmokingAreaValidate.class, metadata, inits);
    }

    public QSmokingAreaValidate(Class<? extends SmokingAreaValidate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.manageAddress = inits.isInitialized("manageAddress") ? new QManageAddress(forProperty("manageAddress")) : null;
    }

}

