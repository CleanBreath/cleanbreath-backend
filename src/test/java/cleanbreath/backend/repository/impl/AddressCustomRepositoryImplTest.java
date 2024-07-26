package cleanbreath.backend.repository.impl;

import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.entity.QAddress;
import cleanbreath.backend.entity.QPath;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressCustomRepositoryImplTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    void findAllAddress() {
        QAddress address = QAddress.address;
        QPath path = QPath.path;

        List<Tuple> result = queryFactory.select(address, path)
                .from(address)
                .join(path)
                .on(address.id.eq(path.id))
                .fetch();

    }
}