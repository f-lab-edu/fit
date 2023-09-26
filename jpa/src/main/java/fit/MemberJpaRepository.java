package fit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends MemberRepository, JpaRepository<MemberEntity, UUID> {

    @Override
    default void createMember(Member member) {
        saveAndFlush(new MemberEntity(member.id(), member.email()));
    }
}
