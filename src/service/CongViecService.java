package src.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import src.entity.CongViec;
import src.repository.CongViecRepository;

@Service
@RequiredArgsConstructor
public class CongViecService {

    final CongViecRepository congViecRepository;

    public List<CongViec> getAllCongViec() {
        return congViecRepository.findAll();
    }
}
