package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import src.entity.TotNghiep;
import src.entity.TotNghiepId;

public interface TotNghiepRepository extends JpaRepository<TotNghiep, TotNghiepId> {

}
