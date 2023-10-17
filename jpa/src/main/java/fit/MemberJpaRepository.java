package fit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends MemberRepository, JpaRepository<MemberEntity, UUID> {

    @Override
    default void createMember(Member member) {
        saveAndFlush(new MemberEntity(member.id(), member.email(), member.passwordHash()));
    }

    @Override
    default Optional<Member> findMemberByEmail(String email) {
        return findByEmail(email)
                    .map(x -> new Member(x.getId(), x.getEmail(), x.getPasswordHash()));
    }

    @Override
    default void deleteMemberByEmail(String email) {
        findByEmail(email).ifPresent(this::delete);
    }


    Optional<MemberEntity> findByEmail(String email);
}
