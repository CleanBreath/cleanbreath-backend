package cleanbreath.backend.entity.manage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QManageAddress is a Querydsl query type for ManageAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManageAddress extends EntityPathBase<ManageAddress> {

    private static final long serialVersionUID = -2011567310L;

    public static final QManageAddress manageAddress = new QManageAddress("manageAddress");

    public final StringPath addressCategory = createString("addressCategory");

    public final StringPath addressName = createString("addressName");

    public final NumberPath<Double> addressPosLat = createNumber("addressPosLat", Double.class);

    public final NumberPath<Double> addressPosLng = createNumber("addressPosLng", Double.class);

    public final StringPath buildingName = createString("buildingName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ManagePath, QManagePath> paths = this.<ManagePath, QManagePath>createList("paths", ManagePath.class, QManagePath.class, PathInits.DIRECT2);

    public final ListPath<SmokingAreaValidate, QSmokingAreaValidate> smokingAreaValidates = this.<SmokingAreaValidate, QSmokingAreaValidate>createList("smokingAreaValidates", SmokingAreaValidate.class, QSmokingAreaValidate.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updateAt = createDateTime("updateAt", java.time.LocalDateTime.class);

    public QManageAddress(String variable) {
        super(ManageAddress.class, forVariable(variable));
    }

    public QManageAddress(Path<? extends ManageAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QManageAddress(PathMetadata metadata) {
        super(ManageAddress.class, metadata);
    }

}

