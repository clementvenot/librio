package com.librio.backend.services;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.librio.backend.entities.User;
import com.librio.backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional; //pour obtenir le read only (permet d'économiser coût cpu memoire)


@Service
@Transactional
class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email déjà utilisé: " + email);
        }
        User u = new User(email,password);
        return userRepository.save(u);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    

	@Override
	public void updatePasswordByEmail(String email, String newPassword) {
	    int updated = userRepository.updatePasswordByEmail(email, newPassword);
	    if (updated == 0) {
	        throw new IllegalArgumentException("Utilisateur introuvable pour l'email: " + email);
	    }
}

}

