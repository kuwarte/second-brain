package com.kuwarte.api.profile;

import com.kuwarte.api.profile.dto.ProfileResponse;
import com.kuwarte.api.profile.dto.UpdateProfileRequest;
import com.kuwarte.api.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfileResponse getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ProfileResponse.toResponse(principal.getUser());
    }

    @PatchMapping
    public ProfileResponse updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ProfileResponse.toResponse(profileService.updateProfile(principal.getUser(), request));
    }
}
