package fit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class GymEntity {

    @Id @GeneratedValue
    private Long id;

    @Setter
    @Column
    private String displayName;

    @Setter
    @Column
    private String region;

    @Setter
    @Column
    private String phone;

    @Setter
    @Column
    private String address;

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private GymCategory category;

    @Setter
    @ManyToOne
    @JoinColumn
    private MemberEntity member;
}