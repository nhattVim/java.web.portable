package src.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import src.entity.LichKham;
import src.repository.LichKhamRepository;
import src.repository.PetRepository;

@Service
@RequiredArgsConstructor
public class LichKhamService {

    final LichKhamRepository lichKhamRepository;
    final PetRepository petRepository;

    public List<LichKham> getAllLichKham() {
        return lichKhamRepository.findAll();
    }

    public List<LichKham> search(String search) {
        return lichKhamRepository
                .findByMaKhamContainingIgnoreCaseOrTenBacSiContainingIgnoreCase(search, search);
    }

    public void addLichKham(LichKham lichKham, String petId) {
        lichKham.setPet(petRepository.findById(petId).get());
        lichKhamRepository.save(lichKham);
    }
}
