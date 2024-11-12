package filum.ai.BackendEngineerIntern.service;

import filum.ai.BackendEngineerIntern.model.entity.Attendance;
import filum.ai.BackendEngineerIntern.model.entity.Event;
import filum.ai.BackendEngineerIntern.model.entity.User;
import filum.ai.BackendEngineerIntern.model.payload.exception.ErrorException;
import filum.ai.BackendEngineerIntern.repository.AttendanceRepository;
import filum.ai.BackendEngineerIntern.repository.EventRepository;
import filum.ai.BackendEngineerIntern.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AuthRepository userRepository;

    public Integer getOrganizerIdByEventId(Integer eventId) {
        return eventRepository.findById(eventId)
                .map(event -> event.getOrganizer().getId())
                .orElse(null);
    }

    public Integer getUserIdByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(User::getId).orElseThrow(() -> new ErrorException("error","Người dùng không tồn tại"));
    }

    public String rsvpToEvent(Integer userId, Integer eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (eventOptional.isEmpty() || userOptional.isEmpty()) {
            throw new ErrorException("error", "Người dùng hoặc sự kiện không tồn tại");
        }

        Event event = eventOptional.get();
        User user = userOptional.get();

        if (attendanceRepository.countByEvent(event) >= event.getCapacity()) {
            throw new ErrorException("error", "Sự kiện đã đạt đến giới hạn số lượng tham gia");
        }

        if (attendanceRepository.existsByEventAndUser(event, user)) {
            throw new ErrorException("error", "Người dùng đã đăng ký tham gia sự kiện này");
        }

        Attendance attendance = new Attendance();
        attendance.setEvent(event);
        attendance.setUser(user);
        attendanceRepository.save(attendance);

        return "Đăng ký tham gia thành công";
    }
}
