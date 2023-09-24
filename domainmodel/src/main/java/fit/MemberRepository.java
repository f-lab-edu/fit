package fit;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {

    void createMember(Member member);

    Optional<Member> findMemberByEmail(String email);
}
