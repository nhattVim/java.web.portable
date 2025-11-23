package src.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class TotNghiepId implements Serializable {

    private String soCMND;
    private String maTruong;
    private String maNganh;
}
