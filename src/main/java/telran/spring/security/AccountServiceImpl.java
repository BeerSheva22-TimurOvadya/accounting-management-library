package telran.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import telran.spring.security.dto.Account;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	final AccountProvider provider;
	
	@Autowired
	UserDetailsService userDetailsManager;
	@Bean
	UserDetailsService getUserDetailsService() {
		return new InMemoryUserDetailsManager();
	}

//TODO
	@Override
	public Account getAccount(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAccount(Account account) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePassword(String username, String newPassword) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAccount(String username) {
		// TODO Auto-generated method stub

	}
	@PostConstruct
	void restoreAccount() {
		
	}
	@PreDestroy
	void saveAccount() {
		
	}

}
