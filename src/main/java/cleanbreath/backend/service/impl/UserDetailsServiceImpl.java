package cleanbreath.backend.service.impl;

import cleanbreath.backend.repository.manage.ManageMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ManageMemberRepository manageMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return manageMemberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found" + username));
    }
}
