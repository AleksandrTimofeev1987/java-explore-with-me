package ru.practicum.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_hits", schema = "public")
@Getter
@Setter
@ToString
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "app_name", nullable = false)
    private String app;

    @Column(name = "hit_uri", nullable = false)
    private String uri;

    @Column(name = "user_ip", nullable = false)
    private String ip;

    @Column(name = "time_stamp", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
