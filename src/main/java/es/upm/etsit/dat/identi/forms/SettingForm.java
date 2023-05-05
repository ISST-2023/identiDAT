package es.upm.etsit.dat.identi.forms;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter @ToString
public class SettingForm implements Serializable {
    
    @NonNull
    private Integer id;

    private String settingKey;

    private String settingName;
    
    @NonNull
    private String settingValue;
}