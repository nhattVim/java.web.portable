package src.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "SINHVIEN")
public class SinhVien {

    @Id
    @Column(name = "SoCMND")
    private String soCMND;

    private String hoTen;
    private String email;
    private String soDT;
    private String diaChi;

    @OneToMany(mappedBy = "sinhVien", cascade = CascadeType.ALL)
    private List<TotNghiep> totNghieps;

    @OneToMany(mappedBy = "sinhVien", cascade = CascadeType.ALL)
    private List<CongViec> congViecs;
}
