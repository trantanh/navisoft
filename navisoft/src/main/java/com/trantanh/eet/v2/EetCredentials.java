package com.trantanh.eet.v2;

import java.nio.file.Path;
import java.util.Objects;

public record EetCredentials(Path certificatePath, char[] certificatePassword) {
    public EetCredentials {
        Objects.requireNonNull(certificatePath, "certificatePath");
        Objects.requireNonNull(certificatePassword, "certificatePassword");
        certificatePassword = certificatePassword.clone();
    }

    @Override
    public char[] certificatePassword() {
        return certificatePassword.clone();
    }
}
