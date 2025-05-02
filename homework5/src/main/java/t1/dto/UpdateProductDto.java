package t1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import t1.entity.ProductType;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateProductDto {
    private BigDecimal balance;
    private ProductType productType;
}
