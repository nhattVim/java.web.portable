package src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import src.entity.LichKham;

public interface LichKhamRepository extends JpaRepository<LichKham, String> {

    List<LichKham> findByMaKhamContainingIgnoreCaseOrTenBacSiContainingIgnoreCase(
            String name, String name2);
}
