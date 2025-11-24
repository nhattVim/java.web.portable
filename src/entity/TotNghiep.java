package src.entity;

import java.time.LocalDate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TOT_NGHIEP")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "sinhVien", "truong", "nganh" })
public class TotNghiep {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private TotNghiepId id;

    @ManyToOne
    @MapsId("soCMND")
    @JoinColumn(name = "SoCMND")
    private SinhVien sinhVien;

    @ManyToOne
    @MapsId("maTruong")
    @JoinColumn(name = "MaTruong")
    private Truong truong;

    @ManyToOne
    @MapsId("maNganh")
    @JoinColumn(name = "MaNganh")
    private Nganh nganh;

    private String heTN;
    private LocalDate ngayTN;
    private String loaiTN;
}
