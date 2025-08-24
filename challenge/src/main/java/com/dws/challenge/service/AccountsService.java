package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;
  private final NotificationService notificationService;

  private final Lock lock = new ReentrantLock();

  @Autowired
  public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
    this.accountsRepository = accountsRepository;
    this.notificationService = notificationService;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public void transfer(TransferRequest request) {
    lock.lock();
    try {
      Account from = accountsRepository.getAccount(request.getAccountFromId());
      Account to = accountsRepository.getAccount(request.getAccountToId());

      if (from == null || to == null) {
        throw new IllegalArgumentException("One or both accounts not found");
      }

      BigDecimal amount = request.getAmount();
      if (amount.signum() <= 0) {
        throw new IllegalArgumentException("Transfer amount must be positive");
      }

      if (from.getBalance().compareTo(amount) < 0) {
        throw new IllegalArgumentException("Insufficient balance");
      }

      from.setBalance(from.getBalance().subtract(amount));
      to.setBalance(to.getBalance().add(amount));

      notificationService.notifyAboutTransfer(from,
              "Transferred " + amount + " to account " + to.getAccountId());
      notificationService.notifyAboutTransfer(to,
              "Received " + amount + " from account " + from.getAccountId());

    } finally {
      lock.unlock();
    }
  }
}
