package src.entity;

import java.util.List;

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
@Table(name = "NGANH")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "totNghieps", "congViecs" })
public class Nganh {

    @Id
    @Column(name = "MaNganh")
    @EqualsAndHashCode.Include
    private String maNganh;

    private String tenNganh;
    private String loaiNganh;

    @OneToMany(mappedBy = "nganh")
    private List<TotNghiep> totNghieps;

    @OneToMany(mappedBy = "nganh")
    private List<CongViec> congViecs;
}
