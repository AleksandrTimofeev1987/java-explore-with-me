package ru.practicum.event.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.user.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "rates", schema = "public")
@Getter
@Setter
@ToString
public class RateEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "event_rate", nullable = false)
    private Integer rate;

    public Long getEventId() {
        return event.getId();
    }
}
