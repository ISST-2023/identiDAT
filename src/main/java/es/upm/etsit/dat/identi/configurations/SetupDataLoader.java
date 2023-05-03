package es.upm.etsit.dat.identi.configurations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.model.Degree;
import es.upm.etsit.dat.identi.persistence.model.Privilege;
import es.upm.etsit.dat.identi.persistence.model.Role;
import es.upm.etsit.dat.identi.persistence.model.Setting;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;
import es.upm.etsit.dat.identi.persistence.repository.DegreeRepository;
import es.upm.etsit.dat.identi.persistence.repository.PrivilegeRepository;
import es.upm.etsit.dat.identi.persistence.repository.RoleRepository;
import es.upm.etsit.dat.identi.persistence.repository.SettingRepository;
import jakarta.transaction.Transactional;

@Component
public class SetupDataLoader implements
  ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CensusMemberRepository cenMemRepo;
 
    @Autowired
    private RoleRepository roleRepo;
 
    @Autowired
    private PrivilegeRepository privRepo;

    @Autowired
    private DegreeRepository dgrRepo;

    @Autowired
    private SettingRepository stngRepo;

    

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if ((stngRepo.findBySettingKey("alreadySetup") != null) ? Boolean.parseBoolean(stngRepo.findBySettingKey("alreadySetup").getSettingValue()) : false)
            return;
        Privilege readPrivilege
          = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
          = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
          Privilege callPrivilege
          = createPrivilegeIfNotFound("CALL_PRIVILEGE");
 
        List<Privilege> adminPrivileges = Arrays.asList(
          readPrivilege, writePrivilege);

        List<Privilege> secretaryPrivileges = Arrays.asList(
          readPrivilege, writePrivilege, callPrivilege);
        
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role secretaryRole = createRoleIfNotFound("ROLE_SECRETARY", secretaryPrivileges);

        if (dgrRepo.findByCode("09DA") == null) dgrRepo.save(new Degree(Long.valueOf(1), "Delegación de Alumnos de Telecomunicación", "09DA", "DAT"));

        cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(1), "Delegado de Alumnos", "ETSIT-UPM", "delegado.alumnos.etsit@upm.es", "delegado.alumnos.etsit", "00000000D", "636980510", dgrRepo.findByCode("09DA"), Arrays.asList(adminRole), Arrays.asList(callPrivilege)));
        cenMemRepo.saveAndFlush(new CensusMember(Long.valueOf(2), "Secretaría de la Delegación de Alumnos", "ETSIT-UPM", "secretaria.dat.etsit@upm.es", "secretaria.dat.etsit", "00000000S", "910671919", dgrRepo.findByCode("09DA"), Arrays.asList(adminRole, secretaryRole), null));

        stngRepo.saveAndFlush(new Setting(1, "alreadySetup", "Primera configuración completada", "true"));
        stngRepo.saveAndFlush(new Setting(2, "filesPath", "Ruta para la subida de ficheros", "./uploads"));
        stngRepo.saveAndFlush(new Setting(3, "academicYear", "Curso académico", "2022-23"));

    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
 
        Privilege privilege = privRepo.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privRepo.saveAndFlush(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
      String name, Collection<Privilege> privileges) {
 
        Role role = roleRepo.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepo.saveAndFlush(role);
        }
        return role;
    }
}