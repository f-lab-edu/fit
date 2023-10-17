package fit;

import java.util.Optional;

public interface MemberRepository {

    void createMember(Member member);

    Optional<Member> findMemberByEmail(String email);

    void updateMember(Member member);

    void deleteMemberByEmail(String email);
}
