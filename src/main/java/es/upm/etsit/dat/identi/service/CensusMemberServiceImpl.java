package es.upm.etsit.dat.identi.service;

import java.util.List;
import java.util.stream.Collectors;

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
        member.setUsername(member.getEmail().split("@")[0]);
        CensusMember cenMembEntity = modelMapper.map(member, CensusMember.class);
        cenMembEntity = cenMemRepo.save(cenMembEntity);
        return modelMapper.map(cenMembEntity, CensusMemberDto.class);
    }

    @Override
    public CensusMemberDto get(Long id) {
        CensusMember cenMembEntity = cenMemRepo.getReferenceById(id);
        CensusMemberDto cenMemDto = modelMapper.map(cenMembEntity, CensusMemberDto.class);
        return cenMemDto;
    }

    @Override
    public CensusMemberDto get(String username) {
        CensusMember cenMembEntity = cenMemRepo.findByUsername(username);
        CensusMemberDto cenMemDto = modelMapper.map(cenMembEntity, CensusMemberDto.class);
        return cenMemDto;
    }

    @Override
    public List<CensusMemberDto> getAll() {
        List<CensusMember> listCenMembEntity = cenMemRepo.findAll();
        List<CensusMemberDto> listCenMembDto = listCenMembEntity.stream().map(cenMemb -> modelMapper.map(cenMemb, CensusMemberDto.class)).collect(Collectors.toList());
        return listCenMembDto;
    }
    
    @Override
    public void delete(Long id) {
        try {
            cenMemRepo.deleteById(id);
        } catch (Exception e) {
            System.out.println("El miembro a borrar no existe");
        }
    }
}
