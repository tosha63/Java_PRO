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
import java.util.UUID;

@Entity
@Table(name = "operations", schema = "test")
@Getter
@Setter
@ToString
public class Operation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_id")
    private UUID operationId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "payment")
    private BigDecimal payment;

    public Operation() {
    }

    public Operation(UUID operationId,
                     Long userId,
                     BigDecimal payment) {
        this.operationId = operationId;
        this.userId = userId;
        this.payment = payment;
    }
}
