package es.upm.etsit.dat.identi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import es.upm.etsit.dat.identi.dto.TokenDto;
import es.upm.etsit.dat.identi.persistence.model.Token;
import es.upm.etsit.dat.identi.persistence.repository.TokenRepository;

@Service
@Validated
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TokenDto create(TokenDto token) {
        Token tokenEntity = modelMapper.map(token, Token.class);
        tokenEntity = tokenRepo.save(tokenEntity);
        return modelMapper.map(tokenEntity, TokenDto.class);
    }
    
}
