package src.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "CONG_VIEC")
public class CongViec {

    @EmbeddedId
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
