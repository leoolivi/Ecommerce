package com.ecommerce.main.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.main.data.AuthResponseDTO;
import com.ecommerce.main.data.LoginRequestDTO;
import com.ecommerce.main.data.PasswordResetCheckDTO;
import com.ecommerce.main.data.PasswordResetRequestDTO;
import com.ecommerce.main.data.RegisterRequestDTO;
import com.ecommerce.main.data.UserManagementDTO;
import com.ecommerce.main.exceptions.OTPCodeNotFoundException;
import com.ecommerce.main.models.OTPCode;
import com.ecommerce.main.services.AppUserService;
import com.ecommerce.main.services.AuthenticationService;
import com.ecommerce.main.services.OTPCodeService;
import com.ecommerce.main.utility.EmailUtility;
import com.ecommerce.main.utility.OTPUtility;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;
    private final EmailUtility emailUtility;
    private final OTPCodeService otpService;
    private final AppUserService userService;

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("password-reset")
    public ResponseEntity<?> passwordResetRequest(@RequestBody PasswordResetRequestDTO requestDTO) {
        OTPCode code = OTPCode.create(OTPUtility.generateCode(), requestDTO.email(), userService.findUserByEmail(requestDTO.email()).getId());
        otpService.createCode(code);
        emailUtility.sendEmail(requestDTO.email(), "Password change", "Here is the OTP code: " + String.valueOf(code.getCode()));
        return ResponseEntity.ok("Email sent to " + requestDTO.email() + "with the code.");
    }
    

    @PostMapping("password-reset-check")
    public ResponseEntity<?> passwordResetCheck(@RequestBody PasswordResetCheckDTO request) throws OTPCodeNotFoundException {
        OTPCode code = otpService.getCodeByCode(request.otp());
        if (otpService.isCodeValid(code)) {
            userService.updateUser(new UserManagementDTO(code.getUserEmail(), request.newPassword(), null));
            return ResponseEntity.ok("Password changed successfully");
        }
        return ResponseEntity.ok("Code not valid");
    }
    
    
}
