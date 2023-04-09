package es.upm.etsit.dat.identi.service;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import jakarta.validation.Valid;
import java.util.List;

public interface CensusMemberService {
    public CensusMemberDto create(@Valid CensusMemberDto member);
    public CensusMemberDto get(Long id);
    public List<CensusMemberDto> getAll();
    public void delete(Long id);
}
