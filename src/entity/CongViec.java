package src.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "CONG_VIEC")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "sinhVien", "nganh" })
public class CongViec {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CongViecId id;

    @ManyToOne
    @MapsId("soCMND")
    @JoinColumn(name = "SoCMND")
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "MaNganh")
    private Nganh nganh;

    private String tenCongViec;
    private String tenCongTy;
    private String diaChiCongTy;
    private Integer thoiGianLamViec;
}
