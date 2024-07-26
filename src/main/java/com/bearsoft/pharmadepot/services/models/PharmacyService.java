package com.bearsoft.pharmadepot.services.models;

import com.bearsoft.pharmadepot.exceptions.pharmacy.PharmacyNotFoundException;
import com.bearsoft.pharmadepot.models.security.SecurityPharmacy;
import com.bearsoft.pharmadepot.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyService implements UserDetailsService {

    private final PharmacyRepository pharmacyRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var pharmacy = pharmacyRepository.findPharmaciesByName(name);

        return pharmacy.map(SecurityPharmacy::new)
                .orElseThrow(() -> new PharmacyNotFoundException(String.format("Username %s not found.",name)));
    }
}
