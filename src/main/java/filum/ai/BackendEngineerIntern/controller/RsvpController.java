package filum.ai.BackendEngineerIntern.controller;

import filum.ai.BackendEngineerIntern.model.payload.response.DataResponse;
import filum.ai.BackendEngineerIntern.repository.AuthRepository;
import filum.ai.BackendEngineerIntern.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rsvp")
public class RsvpController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/{eventId}")
    public ResponseEntity<DataResponse<String>> rsvpToEvent(
            @PathVariable Integer eventId,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(DataResponse.<String>builder()
                            .status("error")
                            .message("Bạn cần đăng nhập để RSVP sự kiện")
                            .build());
        }

        String username = userDetails.getUsername();
        Integer userId = attendanceService.getUserIdByUsername(username);

        String result = attendanceService.rsvpToEvent(userId, eventId);

        Integer organizerId = attendanceService.getOrganizerIdByEventId(eventId);
        if (organizerId != null) {
            String notificationMessage = "Người dùng " + username + " đã tham gia sự kiện của bạn!";
            simpMessagingTemplate.convertAndSendToUser(
                    String.valueOf(organizerId), "/notification", notificationMessage);
        }

        return ResponseEntity.ok(
                DataResponse.<String>builder()
                        .status("success")
                        .message(result)
                        .build()
        );
    }

}
