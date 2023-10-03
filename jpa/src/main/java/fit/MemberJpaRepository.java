package fit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends MemberRepository, JpaRepository<MemberEntity, UUID> {

    @Override
    default void createMember(Member member) {
        saveAndFlush(new MemberEntity(member.id(), member.email(), member.hashingPassword()));
    }

    @Override
    default Optional<Member> findMemberByEmail(String email) {
        return findByEmail(email)
                    .map(x -> new Member(x.getId(), x.getEmail(), x.getHashingPassword()));
    }

    Optional<MemberEntity> findByEmail(String email);
}
