package org.sale;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Pharmacy")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Pharmacy {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private double lat;
    private double lon;
    private boolean onDuty;

}
