package com.stackoverflow.Server.member.service;

import com.stackoverflow.Server.auth.MemberAuthorityUtils;
import com.stackoverflow.Server.exception.BusinessLogicException;
import com.stackoverflow.Server.exception.ExceptionCode;
import com.stackoverflow.Server.member.entity.Member;
import com.stackoverflow.Server.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    private MemberAuthorityUtils authorityUtils;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, MemberAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }

    public Member creatMember(Member member) {
        verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(long userId) {
        Optional<Member> findMember = memberRepository.findById(userId);
        Member responseMember = findMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return responseMember;
    }

    @Transactional(readOnly = true)
    public Page<Member> findMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public void verifyExistsEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if(findMember.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXIST);
    }
}