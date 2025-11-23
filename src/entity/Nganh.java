package src.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "NGANH")
public class Nganh {

    @Id
    @Column(name = "MaNganh")
    private String maNganh;

    private String tenNganh;
    private String loaiNganh;

    @OneToMany(mappedBy = "nganh")
    private List<TotNghiep> totNghieps;

    @OneToMany(mappedBy = "nganh")
    private List<CongViec> congViecs;
}
