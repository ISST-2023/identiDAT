package es.upm.etsit.dat.identi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.validator.internal.engine.validationcontext.ReturnValueExecutableValidationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import es.upm.etsit.dat.identi.persistence.model.CensusMember;
import es.upm.etsit.dat.identi.persistence.repository.CensusMemberRepository;

@Component
@Configurable
public class FieldValidator {

    @Autowired
    CensusMemberRepository cenMemRepo;

    public boolean validateEmail(String email) {
        if (email == null) return false;
        return (email.contains("@") && email.endsWith("upm.es"));
    }

    public boolean validatePhone(String phone) {
        if (phone == null) return false;
        if (!phone.startsWith("+")) phone = "+34" + phone;
        
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneUtil.parse(phone, "");
        } catch (Exception e) {
            return false;
        }
        return phoneUtil.isValidNumber(phoneNumber);
    }

    public boolean validateID(String personalID) {
        if (personalID == null) return false;
        Pattern dniPattern = Pattern.compile("^[0-9]{8}[A-Z]$");
        Pattern niePattern = Pattern.compile("^[XYZ][0-9]{7}[A-Z]$");

        String validLetters = "TRWAGMYFPDXBNJZSQVHLCKE";

        Matcher dniMatcher = dniPattern.matcher(personalID);
        Matcher nieMatcher = niePattern.matcher(personalID);

        if (dniMatcher.find()) {
            return validLetters.charAt(Integer.parseInt(personalID.substring(0, 8)) % 23) == personalID.charAt(8);
        } else if (nieMatcher.find()) {
            String nieCandidate = personalID.charAt(0) == 'X' ? "0"
                        : personalID.charAt(0) == 'Y' ? "1" : "2" + personalID.substring(1, 9);
            
            return validLetters.charAt(Integer.parseInt(nieCandidate.substring(0, 8))) % 23 == nieCandidate.charAt(8);
        } else return false;
    }

    public boolean emailExists(String email) {
        if (email == null) return false;
        return (cenMemRepo.findByEmail(email) != null);
    }

    public boolean IDExists(String personalID) {
        if (personalID == null) return false;
        return (cenMemRepo.findByPersonalID(personalID) != null);
    }

    public boolean emailBelongsToCensusMember(String email, CensusMember censusMember) {
        if (email == null) return false;
        CensusMember retrievedCensusMember = cenMemRepo.findByEmail(email);
        if (retrievedCensusMember != null) return (retrievedCensusMember.equals(censusMember));
        return false;        
    }

    public boolean IDBelongsToCensusMember(String personalID, CensusMember censusMember) {
        if (personalID == null) return false;
        CensusMember retrievedCensusMember = cenMemRepo.findByPersonalID(personalID);
        if (retrievedCensusMember != null) return (retrievedCensusMember.equals(censusMember));
        return false;        
    }
}