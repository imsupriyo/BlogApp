package com.springboot.blog.repository;

import com.springboot.blog.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void givenRoleObject_whenSave_thenReturnSavedRole() {
        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");

        Role savedRole = roleRepository.save(roleAdmin);

        assertThat(savedRole).isNotNull();
    }

    @DisplayName("test to find Role by name")
    @Test
    public void givenRoleObject_whenFindByName_thenReturnRole() {
        assertThat(roleRepository.findByName("ADMIN")).isNotNull();
    }
}
