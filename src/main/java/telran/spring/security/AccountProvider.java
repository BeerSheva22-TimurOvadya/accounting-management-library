package telran.spring.security;

import java.util.List;

import telran.spring.security.dto.Account;

public interface AccountProvider {
List<Account> getAccounts();
void setAccounts(List<Account> accounts);
}
