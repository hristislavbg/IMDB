package com.imdb.movies.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "roles",
        uniqueConstraints = @UniqueConstraint(
                name = "name_unique",
                columnNames = "name"
        )
)
public class Role {
    @Id
    @SequenceGenerator(
            name = "role_sequence",
            sequenceName = "role_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_sequence"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;
}
