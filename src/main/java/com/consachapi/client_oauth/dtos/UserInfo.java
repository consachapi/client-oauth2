package com.consachapi.client_oauth.dtos;

public record UserInfo (
        String sub,
        boolean email_verified,
        String name,
        String preferred_username,
        String email,
        String given_name,
        String family_name,
        String entidad,
        String unidadejecutora,
        String cargo,
        String dependencia
){
}
