package filum.ai.BackendEngineerIntern.service;

import filum.ai.BackendEngineerIntern.model.entity.Event;
import filum.ai.BackendEngineerIntern.model.entity.User;
import filum.ai.BackendEngineerIntern.model.payload.exception.ErrorException;
import filum.ai.BackendEngineerIntern.model.payload.request.EventRequest;
import filum.ai.BackendEngineerIntern.repository.AuthRepository;
import filum.ai.BackendEngineerIntern.repository.EventRepository;
import filum.ai.BackendEngineerIntern.specification.EventSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AuthRepository authRepository;

    public Event createEvent(EventRequest eventRequest, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User organizer = authRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorException("error", "Người tổ chức không tồn tại"));

        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setDate(eventRequest.getDate());
        event.setLocation(eventRequest.getLocation());
        event.setCategory(eventRequest.getCategory());
        event.setCapacity(eventRequest.getCapacity());
        event.setOrganizer(organizer);

        return eventRepository.save(event);
    }

    public Optional<Event> updateEvent(Integer id, EventRequest eventRequest) {
        return eventRepository.findById(id).map(event -> {
            event.setTitle(eventRequest.getTitle());
            event.setDescription(eventRequest.getDescription());
            event.setDate(eventRequest.getDate());
            event.setLocation(eventRequest.getLocation());
            event.setCategory(eventRequest.getCategory());
            event.setCapacity(eventRequest.getCapacity());
            return eventRepository.save(event);
        });
    }

    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

    public Page<Event> getlistEvents(String keyword, String location, String category, Integer organizerId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Event> specification = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            specification = specification.and(EventSpecification.hasKeyword(keyword));
        }
        if (location != null && !location.isEmpty()) {
            specification = specification.and(EventSpecification.hasLocation(location));
        }
        if (category != null && !category.isEmpty()) {
            specification = specification.and(EventSpecification.hasCategory(category));
        }
        if (organizerId != null) {
            specification = specification.and(EventSpecification.hasOrganizerId(organizerId));
        }
        if (startDate != null && endDate != null) {
            specification = specification.and(EventSpecification.hasExactDateRange(startDate, endDate));
        }
        if (startDate != null) {
            specification = specification.and(EventSpecification.hasExactStartDate(startDate));
        }
        if (endDate != null) {
            specification = specification.and(EventSpecification.hasDateBefore(endDate));
        }

        return eventRepository.findAll(specification, pageable);
    }
}
