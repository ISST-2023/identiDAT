package es.upm.etsit.dat.identi.service;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import jakarta.validation.Valid;

public interface CensusMemberService {
    public CensusMemberDto create(@Valid CensusMemberDto member);
}
