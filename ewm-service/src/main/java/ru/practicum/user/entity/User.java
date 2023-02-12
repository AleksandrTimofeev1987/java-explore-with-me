package ru.practicum.user.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Formula("" +
            "(SELECT AVG(r.event_rate) " +
            "FROM rates r " +
            "JOIN events e ON r.event_id = e.event_id " +
            "WHERE e.initiator_id = user_id)")
    private Double rate;
}
