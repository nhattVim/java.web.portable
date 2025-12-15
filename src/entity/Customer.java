package src.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    private String maKhanhHang;
    private String ten;
    private String sdt;
    private String diachi;

    @OneToMany(mappedBy = "maPet")
    private List<Pet> pets;
}
