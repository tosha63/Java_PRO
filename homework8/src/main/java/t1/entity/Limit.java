package t1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "limits", schema = "test")
@Getter
@Setter
@ToString
public class Limit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount_limit")
    private BigDecimal amountLimit;

    public Limit() {
    }

    public Limit(Long userId, BigDecimal amountLimit) {
        this.userId = userId;
        this.amountLimit = amountLimit;
    }
}
