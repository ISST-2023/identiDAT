package es.upm.etsit.dat.identi.forms;

import java.io.Serializable;

import es.upm.etsit.dat.identi.TokenType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class CensusMemberForm implements Serializable {
    private Long censusMemberId;
    private String name;
    private String surname;
    private String email;
    private String personalID;
    private String phone;
    private String degreeCode;
    private String degreeAcronym;
    private String position;
    private Integer diferentiator;
    private TokenType tokenType;
    private String token;
    private Boolean agreement;


    public CensusMemberForm (String name, String surname, String email, String degreeCode, TokenType tokenType, String token) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.degreeCode = degreeCode;
        this.tokenType = tokenType;
        this.token = token;
    }
}
