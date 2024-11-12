package filum.ai.BackendEngineerIntern.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class UserPrincipal {
    public Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrinciple) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            return Integer.parseInt(userPrinciple.getUsername());
        }
        return null;
    }
}
