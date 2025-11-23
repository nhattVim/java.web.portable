package src.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CongViecId implements Serializable {

    private String soCMND;
    private LocalDate ngayVaoCongTy;
}
