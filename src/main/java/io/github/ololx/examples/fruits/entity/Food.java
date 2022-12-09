package io.github.ololx.examples.fruits.entity;

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
@Data
@FieldDefaults(
        level = AccessLevel.PRIVATE
)
@Entity(name = "Fruit")
@Table(name = "fruits")
public class Food {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Food fruit = (Food) o;

        return name.equals(fruit.name);
    }

    @Override
    public int hashCode() {
        return 31 + name.hashCode();
    }
}
