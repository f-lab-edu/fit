package fit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends MemberRepository, JpaRepository<MemberEntity, UUID> {

    @Override
    default void createMember(Member member) {
        saveAndFlush(new MemberEntity(member.id(), member.email(), member.passwordHash(), member.nickname()));
    }

    @Override
    default Optional<Member> findMemberByEmail(String email) {
        return findByEmail(email)
                    .map(x -> new Member(x.getId(), x.getEmail(), x.getPasswordHash(), x.getNickname()));
    }

    @Override
    default void updateMember(Member member) {
        findByEmail(member.email()).ifPresent(memberEntity -> {
            memberEntity.setPasswordHash(member.passwordHash());
            memberEntity.setNickname(member.nickname());
            save(memberEntity);
        });
    }

    @Override
    default void deleteMemberByEmail(String email) {
        findByEmail(email).ifPresent(this::delete);
    }

    Optional<MemberEntity> findByEmail(String email);
}
