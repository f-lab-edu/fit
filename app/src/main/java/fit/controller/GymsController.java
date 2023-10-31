package fit.controller;

import fit.*;
import fit.command.RegisterGymRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class GymsController {

    private final GymJpaRepository repository;

    public GymsController(GymJpaRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/api/gyms/register-gym")
    public ResponseEntity<HashMap<String, Object>> registerGym(@Valid @RequestBody RegisterGymRequest command,
                                                               Authentication authentication) {
        HashMap<String, Object> response = new HashMap<>();

        MemberView memberView = (MemberView) authentication.getPrincipal();
        GymEntity gym = repository.saveAndFlush(createNewGym(command, memberView));
        response.put("id", gym.getId());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/gyms/{id}")
    public ResponseEntity<GymView> getGymById(@PathVariable Long id) {
        Optional<GymEntity> gymEntity = repository.findById(id);

        if (gymEntity.isPresent()) {
            GymEntity gym = gymEntity.get();
            GymView view = new GymView(
                    gym.getId(),
                    gym.getDisplayName(),
                    gym.getRegion(),
                    gym.getPhone(),
                    gym.getAddress(),
                    gym.getCategory(),
                    new MemberView(gym.getMember().getId(), gym.getMember().getEmail())
            );
            return ResponseEntity.ok().body(view);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private GymEntity createNewGym(RegisterGymRequest command, MemberView view) {
        GymEntity gym = new GymEntity();
        gym.setDisplayName(command.displayName());
        gym.setRegion(command.region());
        gym.setPhone(command.phone());
        gym.setAddress(command.address());
        gym.setCategory(command.category());
        gym.setMember(new MemberEntity(view.id(), view.email(), null));
        return gym;
    }
}