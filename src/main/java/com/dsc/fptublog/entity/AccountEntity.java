package com.dsc.fptublog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class AccountEntity {
    protected String id;
    protected String email;
    protected String alternativeEmail;
    protected String firstName;
    protected String lastName;
    protected String password;
    protected String avatarUrl;
    protected String description;
    protected AccountStatusEntity status;

    public AccountEntity(AccountEntity account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.alternativeEmail = account.getAlternativeEmail();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.password = account.getPassword();
        this.avatarUrl = account.getAvatarUrl();
        this.description = account.getDescription();
        this.status = account.getStatus();
    }
}