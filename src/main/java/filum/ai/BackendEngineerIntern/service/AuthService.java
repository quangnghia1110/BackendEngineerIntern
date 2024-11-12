package filum.ai.BackendEngineerIntern.service;

import filum.ai.BackendEngineerIntern.model.entity.User;
import filum.ai.BackendEngineerIntern.model.payload.request.RegisterRequest;
import filum.ai.BackendEngineerIntern.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại trước đó");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}
