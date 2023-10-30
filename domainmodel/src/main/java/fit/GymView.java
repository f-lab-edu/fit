package fit;

public record GymView(
        Long id,
        String displayName,
        String region,
        String phone,
        String address,
        GymCategory category,
        MemberView owner
) { }