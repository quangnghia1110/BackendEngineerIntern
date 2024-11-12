package filum.ai.BackendEngineerIntern.repository;

import filum.ai.BackendEngineerIntern.model.entity.Attendance;
import filum.ai.BackendEngineerIntern.model.entity.Event;
import filum.ai.BackendEngineerIntern.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    int countByEvent(Event event);

    boolean existsByEventAndUser(Event event, User user);
}
