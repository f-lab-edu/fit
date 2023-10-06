package fit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class MemberEntity {

    @Id
    private UUID id;
    @Column(unique = true)
    private String email;
    @Column
    private String passwordHash;
}
