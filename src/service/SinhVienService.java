package src.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import src.entity.SinhVien;
import src.entity.TotNghiep;
import src.repository.SinhVienRepository;
import src.repository.TotNghiepRepository;

@Service
@RequiredArgsConstructor
public class SinhVienService {

    final SinhVienRepository sinhVienRepository;
    final TotNghiepRepository totNghiepRepository;

    @Transactional(readOnly = true)
    public List<SinhVien> getAllSinhVien() {
        return sinhVienRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<SinhVien> search(String search) {
        // if (search.isEmpty()) {
        // return sinhVienRepository.findAll();
        // }
        //
        // List<SinhVien> result =
        // sinhVienRepository.findBySoCMNDContainingIgnoreCase(search);
        //
        // if (!result.isEmpty()) {
        // return result;
        // }
        //
        // result = sinhVienRepository.findByHoTenContainingIgnoreCase(search);
        //
        // if (!result.isEmpty()) {
        // return result;
        // }
        //
        // result = sinhVienRepository.findBySoDTContainingIgnoreCase(search);
        //
        // if (!result.isEmpty()) {
        // return result;
        // }
        //
        // result = sinhVienRepository.findByDiaChiContainingIgnoreCase(search);
        //
        // if (!result.isEmpty()) {
        // return result;
        // }
        //
        // result = sinhVienRepository.findByEmailContainingIgnoreCase(search);
        // return result;

        return sinhVienRepository
                .findBySoCMNDContainingIgnoreCaseOrHoTenContainingIgnoreCaseOrSoDTContainingIgnoreCaseOrDiaChiContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        search, search, search, search, search);
    }

    @Transactional
    public void addSinhVien(SinhVien student) {
        sinhVienRepository.save(student);
    }

    @Transactional
    public void addTotNghiep(TotNghiep totNghiep) {
        totNghiepRepository.save(totNghiep);
    }

    @Transactional
    public void addSinhVienVaTotNghiep(SinhVien sinhVien, TotNghiep totNghiep) {
        sinhVienRepository.save(sinhVien);
        totNghiep.setSinhVien(sinhVien);
        totNghiepRepository.save(totNghiep);
    }

}
