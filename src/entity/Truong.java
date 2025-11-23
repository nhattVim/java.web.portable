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
@Table(name = "TRUONG")
public class Truong {

    @Id
    @Column(name = "MaTruong")
    private String maTruong;

    private String tenTruong;
    private String diaChi;
    private String soDT;

    @OneToMany(mappedBy = "truong")
    private List<TotNghiep> totNghieps;
}
