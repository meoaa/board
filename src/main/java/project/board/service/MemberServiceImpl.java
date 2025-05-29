package project.board.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.board.domain.Member;
import project.board.dto.member.MemberResponseDto;
import project.board.dto.member.MemberSignUpRequestDto;
import project.board.exception.DuplicateMemberException;
import project.board.exception.ExistEmailException;
import project.board.exception.ExistUsernameException;
import project.board.exception.MemberNotFoundException;

import project.board.repository.MemberJpaRepository;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberJpaRepository memberRepository;


    @Transactional
    @Override
    public void addMember(MemberSignUpRequestDto dto) {
        validateDuplicate(dto);

        Member member = Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();
        try{
            memberRepository.save(member);
        } catch(DataIntegrityViolationException e){
            throw new DuplicateMemberException();
        }
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

    private void validateDuplicate(MemberSignUpRequestDto dto) {
        if(memberRepository.existsByEmail(dto.getEmail()))
            throw new ExistEmailException();
        if(memberRepository.existsByUsername(dto.getUsername()))
            throw new ExistUsernameException();
    }

    private Member findMemberById(long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

}
