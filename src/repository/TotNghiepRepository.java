package src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import src.entity.TotNghiep;
import src.entity.TotNghiepId;

public interface TotNghiepRepository extends JpaRepository<TotNghiep, TotNghiepId> {

    List<TotNghiep> findBySinhVienSoCMNDContainingIgnoreCaseOrSinhVienHoTenContainingIgnoreCaseOrTruongMaTruongContainingIgnoreCaseOrNganhMaNganhContainingIgnoreCase(
            String search, String search2, String search3, String search4);
}
