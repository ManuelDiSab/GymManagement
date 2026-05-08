package org.example.gym_management.security.runner;

import java.util.List;

import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.repository.RoleRepository;

import org.example.gym_management.security.repository.UserRepository;
import org.example.gym_management.security.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthRunner implements ApplicationRunner {
	
	@Autowired RoleRepository roleRepository;
	@Autowired
	AuthServiceImpl authService;
	@Autowired UserRepository userRepository;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Role> roleList = roleRepository.findAll();
		if(roleList.isEmpty()) { // Solo se nono ci sono ruoli salvati | Only if there are not roles saved
			// Serve per inizializzare i ruoli nel DB | Initialize the roles in database
			setRoleDefault();
		}
		Role admin =roleRepository.findByRoleName(ERole.ROLE_ADMIN).orElse(null);
		if( userRepository.findAll().isEmpty() || userRepository.findByRolesContaining(admin).isEmpty()) {
			setSuperAdmin();
		}else{
			userRepository.findByRolesContaining(admin).forEach(User -> System.out.println(User.getUsername()) );
		}
	}

	private void setRoleDefault() {
		// Creo un ruolo Admin e lo salvo nel DB | Create role Admin and save it in DB
		Role admin = new Role();
		admin.setRoleName(ERole.ROLE_ADMIN);
		roleRepository.save(admin);

		// Creo un ruolo User e lo salvo nel DB | Create role Client and save it in DB
		Role client = new Role();
		client.setRoleName(ERole.ROLE_CLIENT);
		roleRepository.save(client);

		// Creo un ruolo Instructor e lo salvo nel DB | Create role Instructor and save it in DB
		Role instructor = new Role();
		instructor.setRoleName(ERole.ROLE_INSTRUCTOR);
		roleRepository.save(instructor);
	}

	private void setSuperAdmin() {
		authService.createAdmin();
	}

}
