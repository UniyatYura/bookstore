package mate.academy.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.repository.RoleRepository;
import mate.academy.bookstore.service.RoleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(Role.RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(() ->
                new RuntimeException("Can't find role by roleName"));
    }
}
