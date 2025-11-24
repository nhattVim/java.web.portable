package src.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SINHVIEN")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "totNghieps", "congViecs" })
public class SinhVien {

    @Id
    @Column(name = "SoCMND")
    @EqualsAndHashCode.Include
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
