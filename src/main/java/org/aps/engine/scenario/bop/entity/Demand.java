package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.aps.engine.execution.entity.Part;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Demand {
    @Id
    private Long id;
    @ManyToOne
    private Part part;

    private Integer quantity;
    private LocalDate dueDate;
}
