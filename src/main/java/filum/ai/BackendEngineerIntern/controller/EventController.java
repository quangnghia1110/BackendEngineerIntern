package filum.ai.BackendEngineerIntern.controller;

import filum.ai.BackendEngineerIntern.model.entity.Event;
import filum.ai.BackendEngineerIntern.model.entity.User;
import filum.ai.BackendEngineerIntern.model.payload.request.EventRequest;
import filum.ai.BackendEngineerIntern.repository.AuthRepository;
import filum.ai.BackendEngineerIntern.service.EventService;
import filum.ai.BackendEngineerIntern.model.payload.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private AuthRepository authRepository;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<DataResponse<Event>> createEvent(
            @RequestBody EventRequest eventRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(DataResponse.<Event>builder()
                            .status("error")
                            .message("Bạn cần đăng nhập để tạo sự kiện")
                            .build());
        }

        Event createdEvent = eventService.createEvent(eventRequest, userDetails);

        DataResponse<String> responseDTO = DataResponse.<String>builder()
                .status("success")
                .message("Sự kiện mới " + createdEvent.getTitle() + "mời bạn tham gia")
                .build();

        List<User> usersWithRoleUser = authRepository.findByRole("ROLE_USER");
        usersWithRoleUser.forEach(user -> {
            simpMessagingTemplate.convertAndSendToUser(
                    String.valueOf(user.getId()), "/notification", responseDTO);
        });

        return ResponseEntity.ok(
                DataResponse.<Event>builder()
                        .status("success")
                        .message("Tạo sự kiện thành công")
                        .data(createdEvent)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<DataResponse<Event>> updateEvent(@PathVariable Integer id, @RequestBody EventRequest eventRequest, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(DataResponse.<Event>builder()
                            .status("error")
                            .message("Bạn cần đăng nhập để cập nhật sự kiện")
                            .build());
        }
        return eventService.updateEvent(id, eventRequest)
                .map(updatedEvent -> {

                    return ResponseEntity.ok(DataResponse.<Event>builder()
                            .status("success")
                            .message("Cập nhật sự kiện thành công")
                            .data(updatedEvent)
                            .build());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataResponse.<Event>builder()
                                .status("error")
                                .message("Sự kiện không tồn tại")
                                .build()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<DataResponse<String>> deleteEvent(@PathVariable Integer id,@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(DataResponse.<String>builder()
                            .status("error")
                            .message("Bạn cần đăng nhập để xóa sự kiện")
                            .build());
        }
        eventService.deleteEvent(id);
        return ResponseEntity.ok(
                DataResponse.<String>builder()
                        .status("success")
                        .message("Xóa sự kiện thành công")
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<DataResponse<Page<Event>>> listEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer organizerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(DataResponse.<Page<Event>>builder()
                            .status("error")
                            .message("Bạn cần đăng nhập để xem danh sách sự kiện")
                            .build());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        Page<Event> events = eventService.getlistEvents(
                keyword, location, category, organizerId, startDate, endDate, pageable);


        if (events.isEmpty()) {
            return ResponseEntity.ok(
                    DataResponse.<Page<Event>>builder()
                            .status("success")
                            .message("Không tìm thấy sự kiện nào phù hợp")
                            .data(events)
                            .build()
            );
        }

        return ResponseEntity.ok(
                DataResponse.<Page<Event>>builder()
                        .status("success")
                        .message("Lấy danh sách sự kiện thành công")
                        .data(events)
                        .build()
        );
    }
}
