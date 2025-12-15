package src.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "LichKham")
@Getter
@Setter
@NoArgsConstructor
public class LichKham {

    @Id
    private String maKham;
    private LocalDate ngayKham;
    private String tenBacSi;
    private String tinhTrangSucKhoe;
    private String chuanDoanVaHuongDieuTri;

    @ManyToOne
    @JoinColumn(name = "MaPet")
    private Pet pet;
}
