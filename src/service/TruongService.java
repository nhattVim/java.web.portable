package src.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import src.entity.Truong;
import src.repository.TruongRepository;

@Service
@RequiredArgsConstructor
public class TruongService {

    final TruongRepository truongRepository;

    public List<Truong> getAllTruong() {
        return truongRepository.findAll();
    }

    public Truong getTruongById(String maTruong) {
        return truongRepository.findById(maTruong).get();
    }
}
