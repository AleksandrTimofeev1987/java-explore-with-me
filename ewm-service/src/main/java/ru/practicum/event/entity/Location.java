package ru.practicum.event.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations", schema = "public")
@Getter
@Setter
@ToString
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "location_lat", nullable = false)
    private Float lat;

    @Column(name = "location_lon", nullable = false)
    private Float lon;
}
