package filum.ai.BackendEngineerIntern.model.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances;
}
