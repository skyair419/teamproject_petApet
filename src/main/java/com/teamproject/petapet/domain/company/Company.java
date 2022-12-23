package com.teamproject.petapet.domain.company;

import com.teamproject.petapet.domain.member.Authority;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Company {

    @Id
    private String companyId;

    @Column(length = 100, nullable = false)
    private String companyPw;

    @Column(length = 45, nullable = false)
    private String companyName;

    @Column(length = 45, nullable = false, unique = true)
    private String companyEmail;

    @Column(length = 45, nullable = false)
    private String companyNumber;

    @Column(length = 45, nullable = false, unique = true)
    private String companyPhoneNum;

    @Column(updatable = false)
    private LocalDateTime companyJoinDate;

    //활성화 컬럼
    @Column(nullable = false)
    private boolean activated;

    @OneToMany(mappedBy = "company", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }

}