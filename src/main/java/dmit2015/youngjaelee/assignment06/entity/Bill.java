package dmit2015.youngjaelee.assignment06.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ylee39Bill")
@Getter
@Setter
@NoArgsConstructor
public class Bill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "The field Payee Name is required")
    @Column(nullable = false)
    @Size(min = 3, message = "The field Payee Name must be a string with a minimum length of 3")
    String payeeName;

    @Column(nullable = false)
    @NotNull(message = "The field Due Date is required")
    LocalDate dueDate;

    @Column(nullable = false)
    @NotNull(message = "The field Amount Due is required")
    BigDecimal amountDue;

    BigDecimal amountBalance;

    @Version
    Integer version;

    @JsonbTransient
    private LocalDateTime createdDateTime;

    @JsonbTransient
    private LocalDateTime lastModifiedDateTime;

    @PrePersist
    private void beforePersist() {
        createdDateTime = LocalDateTime.now();
        lastModifiedDateTime = createdDateTime;
    }

    @PreUpdate
    private void beforeUpdate() {
        lastModifiedDateTime = LocalDateTime.now();
    }
}
