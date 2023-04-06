package es.upm.etsit.dat.identi.service;


import es.upm.etsit.dat.identi.dto.TokenDto;
import jakarta.validation.Valid;

public interface TokenService {
    public TokenDto create(@Valid TokenDto token);
}
