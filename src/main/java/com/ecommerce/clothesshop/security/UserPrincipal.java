package com.ecommerce.clothesshop.security;

import com.ecommerce.clothesshop.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private Long userId;
    private String email;
    private UserRole role;
}
