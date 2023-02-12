package ru.practicum.compilation.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.event.entity.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations", schema = "public")
@Getter
@Setter
@ToString
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "compilation_title", nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(name = "compilation_event",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
}
