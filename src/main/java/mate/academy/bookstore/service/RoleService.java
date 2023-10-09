package mate.academy.bookstore.service;

import mate.academy.bookstore.model.Role;

public interface RoleService {
    Role getRoleByRoleName(Role.RoleName roleName);
}
