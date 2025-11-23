package src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import src.entity.CongViec;
import src.entity.CongViecId;

public interface CongViecRepository extends JpaRepository<CongViec, CongViecId> {

    List<CongViec> findByTenCongViecContainingIgnoreCaseOrTenCongTyContainingIgnoreCase(
            String search, String search2);
}
