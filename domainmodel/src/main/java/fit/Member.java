package fit;

import java.util.UUID;

public record Member(UUID id, String email, String hashingPassword) {
}
