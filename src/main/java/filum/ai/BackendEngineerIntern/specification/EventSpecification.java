package filum.ai.BackendEngineerIntern.specification;

import filum.ai.BackendEngineerIntern.model.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Date;

public class EventSpecification {

    public static Specification<Event> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("description"), "%" + keyword + "%")
                );
    }

    public static Specification<Event> hasLocation(String location) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("location"), location);
    }

    public static Specification<Event> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Event> hasOrganizerId(Integer organizerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("organizer").get("id"), organizerId);
    }

    public static Specification<Event> hasExactStartDate(LocalDate startDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("date").as(LocalDate.class), startDate);
    }

    public static Specification<Event> hasDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("date").as(LocalDate.class), endDate);
    }

    public static Specification<Event> hasExactDateRange(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("date").as(LocalDate.class), startDate),
                criteriaBuilder.lessThanOrEqualTo(root.get("date").as(LocalDate.class), endDate)
        );
    }

}
