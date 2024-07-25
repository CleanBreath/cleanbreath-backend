package cleanbreath.backend.service.impl;

import cleanbreath.backend.dto.PathDTO.ResponsePathDTO;
import cleanbreath.backend.entity.Path;
import cleanbreath.backend.repository.PathRepository;
import cleanbreath.backend.service.PathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class PathServiceImpl implements PathService {
    private final PathRepository repository;

    public List<ResponsePathDTO> getAllPaths() {
        List<Path> findAllPath = repository.findAll();
        return findAllPath.stream()
                .map(ResponsePathDTO::new)
                .toList();
    }

    public ResponsePathDTO findAddressToPath(Long addressId) {
        Path findPath = repository.findByAddressId(addressId);
        return new ResponsePathDTO(findPath);
    }


}
