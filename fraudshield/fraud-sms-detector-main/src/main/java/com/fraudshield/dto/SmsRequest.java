package com.fraudshield.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsRequest {

    @NotBlank(message = "Message must not be blank")
    @Size(min = 1, max = 2000, message = "Message must be between 1 and 2000 characters")
    private String message;

    @NotBlank(message = "Sender must not be blank")
    @Size(min = 1, max = 100, message = "Sender must be between 1 and 100 characters")
    private String sender;
}