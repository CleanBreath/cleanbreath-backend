package cleanbreath.backend.service.impl;

import cleanbreath.backend.entity.Address;
import cleanbreath.backend.entity.DivisionArea;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.repository.AddressRepository;
import cleanbreath.backend.repository.PathRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AddressServiceImplTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PathRepository pathRepository;

    @Test
    @Transactional
    void saveAddress() {
        List<Path> paths = new ArrayList<>();

        Address addressA = Address.builder()
                .addressName("경기 안양시 동안구 평촌동 895")
                .buildingName("평촌중앙공원")
                .addressCategory("도시공원")
                .addressPosLat(37.390005005221774)
                .addressPosLng(126.95920049292776)
                .updateAt(LocalDateTime.now())
                .build();

        Address saveAddress = addressRepository.save(addressA);

        Path pathA = Path.builder()
                .divisionArea(DivisionArea.NON_SMOKING_ZONE)
                .address(addressA)
                .pathLat("TestLat")
                .pathLng("TestLng")
                .build();

    }

    @Test
    void deleteAddress() {
    }
}