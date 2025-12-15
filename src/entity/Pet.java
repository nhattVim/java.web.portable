package src.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pet")
@Getter
@Setter
@NoArgsConstructor
public class Pet {

    @Id
    private String maPet;
    private String ten;
    private LocalDate ngaySinh;
    private String type;
    private String subType;

    @ManyToOne
    @JoinColumn(name = "maKhanhHang")
    private Customer khanhHang;

    @OneToMany(mappedBy = "maKham")
    private List<LichKham> lichKhams;
}
