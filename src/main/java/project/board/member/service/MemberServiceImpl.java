package project.board.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.board.common.exception.*;
import project.board.member.domain.Member;
import project.board.auth.dto.SignUpRequestDto;
import project.board.auth.dto.SignUpResponseDto;
import project.board.member.dto.MemberResponseDto;
import project.board.member.dto.MemberUpdateProfileRequestDto;
import project.board.member.dto.PasswordChangeRequestDto;
import project.board.member.repository.MemberJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberJpaRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public SignUpResponseDto addMember(SignUpRequestDto dto) {
        validateDuplicate(dto);
        Member member = Member.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();
        try{
            Member savedMember = memberRepository.save(member);
            return SignUpResponseDto.from(savedMember);
        } catch(DataIntegrityViolationException e){
            throw new DuplicateMemberException();
        }
    }

    @Transactional
    @Override
    public MemberResponseDto updateMember(
            Long id, MemberUpdateProfileRequestDto dto) {
        Member member = findMemberById(id);

        if(memberRepository.existsByEmail(dto.getEmail()))
            throw new ExistEmailException();

        member.updateProfile(dto);

        return new MemberResponseDto(member);
    }

    //TODO : security 도입시 변경
    @Transactional
    @Override
    public MemberResponseDto changePassword(
            Long id,
            PasswordChangeRequestDto dto) {

        Member member = findMemberById(id);

        if(!passwordEncoder.matches(dto.getOldPassword(), member.getPassword())){
            throw new MissMatchOldPassword();
        }

        //TODO : dto의 password를 hash화 하는 로직
        dto.setNewPassword(passwordEncoder.encode(dto.getNewPassword()));

        member.changePassword(dto);
        return new MemberResponseDto(member);
    }

    @Override
    public MemberResponseDto searchMemberById(long id) {
        Member member = findMemberById(id);
        return new MemberResponseDto(member);
    }

    @Override
    public List<MemberResponseDto> searchAllMember() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteMember(long id) {
        Member member = findMemberById(id);
        memberRepository.delete(member);
    }

    private void validateDuplicate(SignUpRequestDto dto) {
        if(memberRepository.existsByEmail(dto.getEmail()))
            throw new ExistEmailException();
        if(memberRepository.existsByUsername(dto.getUsername()))
            throw new ExistUsernameException();
    }

    private Member findMemberById(long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

}
