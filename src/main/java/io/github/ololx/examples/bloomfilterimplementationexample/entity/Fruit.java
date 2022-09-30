package io.github.ololx.examples.bloomfilterimplementationexample.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * project bloom-filter-implementation-example
 * created 30.09.2022 13:50
 *
 * @author Alexander A. Kropotin
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@FieldDefaults(
        level = AccessLevel.PRIVATE
)
@Entity(name = "SomeData")
@Table(name = "some_data")
public class Fruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            insertable = false,
            nullable = false
    )
    Integer id;

    @Column(
            name = "name",
            nullable = false
    )
    String name;
}
