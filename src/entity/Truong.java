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
@Table(name = "TRUONG")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "totNghieps")
public class Truong {

    @Id
    @Column(name = "MaTruong")
    @EqualsAndHashCode.Include
    private String maTruong;

    private String tenTruong;
    private String diaChi;
    private String soDT;

    @OneToMany(mappedBy = "truong")
    private List<TotNghiep> totNghieps;
}
