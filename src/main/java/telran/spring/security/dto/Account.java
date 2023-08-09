package telran.spring.security.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Account {

  final String username;
  final String password;
  final String[] roles;
  LocalDate expDate;
}


