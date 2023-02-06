package ru.practicum.event.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Embeddable
@AttributeOverrides({
        @AttributeOverride( name = "lat", column = @Column(name = "location_lat")),
        @AttributeOverride( name = "lon", column = @Column(name = "location_lon"))
})
public class Location {

    private Float lat;

    private Float lon;
}
