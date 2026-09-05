package com.kuwarte.api.preferences;

import com.kuwarte.api.preferences.dto.UpdateUserPreferencesRequest;
import com.kuwarte.api.preferences.dto.UserPreferencesResponse;
import com.kuwarte.api.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class UserPreferencesController {

    private final UserPreferencesService preferencesService;

    @GetMapping
    public UserPreferencesResponse get(@AuthenticationPrincipal UserPrincipal principal) {
        UserPreferences preferences = preferencesService.getForUser(principal.getUser());
        return UserPreferencesResponse.toResponse(preferences);
    }

    @PatchMapping
    public UserPreferencesResponse update(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateUserPreferencesRequest request) {

        UserPreferences preferences = preferencesService.update(principal.getUser(), request);
        return UserPreferencesResponse.toResponse(preferences);
    }
}
