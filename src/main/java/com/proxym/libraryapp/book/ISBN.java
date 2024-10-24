package com.proxym.libraryapp.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter

public class ISBN {
    long isbnCode;

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }
}
