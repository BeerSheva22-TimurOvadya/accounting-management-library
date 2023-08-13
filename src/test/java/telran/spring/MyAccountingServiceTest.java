package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.TestPropertySource;

import telran.spring.exceptions.NotFoundException;
import telran.spring.security.AccountProviderImpl;
import telran.spring.security.AccountService;
import telran.spring.security.AccountServiceImpl;
import telran.spring.security.AccountingConfiguration;
import telran.spring.security.dto.Account;

@SpringBootTest(classes = { AccountProviderImpl.class, AccountServiceImpl.class, AccountingConfiguration.class })
@TestPropertySource(properties = { "app.security.admin.password=ppp", "app.security.admin.username=admin", "app.expiration.period.seconds=1" })
class MyAccountingServiceTest {
	
	@Value("${app.expiration.period.seconds:0}")
	private int expirationPeriodSeconds;

	@Autowired
	AccountService accountService;

	@Autowired
	UserDetailsManager userDetailsManager;

	private final String testUsername = "testUser";
	private final String testPassword = "testPassword123";
	private final String[] testRoles = { "USER" };

	@BeforeEach
	void setUp() {
		try {
			accountService.deleteAccount(testUsername);
		} catch (NotFoundException e) {
		}
	}

	@Test
	void adminExistsTest() {
		assertTrue(userDetailsManager.userExists("admin"));
	}

	@Test
	void addAccountTest() {
		Account account = new Account(testUsername, testPassword, testRoles);
		accountService.addAccount(account);

		assertTrue(userDetailsManager.userExists(testUsername));		
	}

	@Test
	void addExistingAccountTest() {
		Account account = new Account(testUsername, testPassword, testRoles);
		accountService.addAccount(account);

		assertThrows(IllegalStateException.class, () -> accountService.addAccount(account));
		
	}

	@Test
	void getAccountTest() {
		Account account = new Account(testUsername, testPassword, testRoles);
		accountService.addAccount(account);

		assertNotNull(accountService.getAccount(testUsername));
	}

	@Test
	void getNonExistingAccountTest() {
		assertThrows(NotFoundException.class, () -> accountService.getAccount(testUsername));
	}

	@Test
	void updatePasswordTest() {
		Account account = new Account(testUsername, testPassword, testRoles);
		accountService.addAccount(account);

		accountService.updatePassword(testUsername, "newPassword123");
	}

	@Test
	void updatePasswordForNonExistingAccountTest() {
		assertThrows(NotFoundException.class, () -> accountService.updatePassword(testUsername, "newPassword123"));
	}

	@Test
	void updateWithOldPasswordTest() {
		Account account = new Account(testUsername, testPassword, testRoles);
		accountService.addAccount(account);

		assertThrows(IllegalStateException.class, () -> accountService.updatePassword(testUsername, testPassword));
	}

	@Test
	void deleteAccountTest() {
		Account account = new Account(testUsername, testPassword, testRoles);
		accountService.addAccount(account);
		accountService.deleteAccount(testUsername);

		assertFalse(userDetailsManager.userExists(testUsername));
	}

	@Test
	void deleteNonExistingAccountTest() {
		assertThrows(NotFoundException.class, () -> accountService.deleteAccount(testUsername));
	}
	
	@Test
	void accountExpirationTest() throws InterruptedException {
	    Account account = new Account(testUsername, testPassword, testRoles);
	    accountService.addAccount(account);	        
	    Thread.sleep((expirationPeriodSeconds + 1) * 1000);	   
	    assertTrue(userDetailsManager.userExists(testUsername));
	    UserDetails userDetails = userDetailsManager.loadUserByUsername(testUsername);
	    assertTrue(userDetails.isAccountNonExpired());
	}

	    @Test
	    void passwordHistoryTest() {
	        Account account = new Account(testUsername, testPassword, testRoles);
	        accountService.addAccount(account);	        
	        accountService.updatePassword(testUsername, "newPassword1");
	        accountService.updatePassword(testUsername, "newPassword2");
	        accountService.updatePassword(testUsername, "newPassword3");	        
	        assertThrows(IllegalStateException.class, () -> accountService.updatePassword(testUsername, "newPassword1"));
	        assertThrows(IllegalStateException.class, () -> accountService.updatePassword(testUsername, "newPassword2"));
	    }
	}
	
	

