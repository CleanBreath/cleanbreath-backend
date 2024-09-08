package cleanbreath.backend.entity.manage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QManageMember is a Querydsl query type for ManageMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManageMember extends EntityPathBase<ManageMember> {

    private static final long serialVersionUID = 1111120796L;

    public static final QManageMember manageMember = new QManageMember("manageMember");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath username = createString("username");

    public QManageMember(String variable) {
        super(ManageMember.class, forVariable(variable));
    }

    public QManageMember(Path<? extends ManageMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QManageMember(PathMetadata metadata) {
        super(ManageMember.class, metadata);
    }

}

