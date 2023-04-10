package es.upm.etsit.dat.identi;

import java.io.Serializable;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class User implements Serializable {

    @NonNull
    private String username;

    @NonNull
    private boolean isAdmin;

}