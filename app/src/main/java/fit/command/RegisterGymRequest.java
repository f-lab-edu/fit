package fit.command;

import fit.GymCategory;
import jakarta.validation.constraints.NotNull;

public record RegisterGymRequest(
        @NotNull String displayName,
        @NotNull String region,
        String phone,
        @NotNull String address,
        @NotNull GymCategory category
) { }