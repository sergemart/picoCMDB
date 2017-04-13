package com.github.sergemart.picocmdb.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.github.sergemart.picocmdb.domain.Role;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.service.RoleService;


@RestController
@RequestMapping("/rest/roles")
public class RoleRestController {

    @Autowired
    private RoleService roleService;

    // -------------- READ --------------

	@RequestMapping(method = RequestMethod.GET)
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{roleId}")
    public Role getRole(@PathVariable("roleId") String roleId) throws NoSuchObjectException {
        return roleService.getRole(roleId.toUpperCase());
    }

}
