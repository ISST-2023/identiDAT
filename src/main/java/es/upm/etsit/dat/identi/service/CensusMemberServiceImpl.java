package es.upm.etsit.dat.identi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import es.upm.etsit.dat.identi.dto.CensusMemberDto;
import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;

@Service
@Validated
public class CensusMemberServiceImpl implements CensusMemberService {

    @Autowired
    private CensusMemberRepository cenMemRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CensusMemberDto create(CensusMemberDto member) {
        CensusMember cenMembEntity = modelMapper.map(member, CensusMember.class);
        cenMembEntity = cenMemRepo.save(cenMembEntity);
        return modelMapper.map(cenMembEntity, CensusMemberDto.class);
    }
    
}
