package project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "planned_shopping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannedShopping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_planned_shopping")
    @SequenceGenerator(name = "seq_planned_shopping", allocationSize = 5)
    @EqualsAndHashCode.Exclude
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "comment", nullable = false, length = 10000)
    private String comment;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "isMorning", nullable = false)
    private Boolean isMorning;
}
