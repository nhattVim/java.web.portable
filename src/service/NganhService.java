package src.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import src.entity.Nganh;
import src.repository.NganhRepository;

@Service
@RequiredArgsConstructor
public class NganhService {

    final NganhRepository nganhRepository;

    public List<Nganh> getAllNganh() {
        return nganhRepository.findAll();
    }

    public Nganh getNganhById(String maNganh) {
        return nganhRepository.findById(maNganh).get();
    }

}
