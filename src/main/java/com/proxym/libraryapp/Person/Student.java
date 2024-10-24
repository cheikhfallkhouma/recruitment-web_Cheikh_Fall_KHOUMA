package com.proxym.libraryapp.Person;

import com.proxym.libraryapp.member.Member;

public class Student extends Member {

    private boolean isFirstYear = true;

    public Student(boolean isFirstYear, float initialWallet) {
        this.isFirstYear = isFirstYear;
        setWallet(initialWallet);
    }

    @Override
    public void payBook(int numberOfDays) {
        float price = 0.0f;
        if (isFirstYear && numberOfDays <= 15) {
            price = 0;
        } else {
            // Calcul of the price that the student must pay
            price = numberOfDays * 0.10f;
        }
        setWallet(getWallet() - price);
        }

    public boolean isFirstYear() {
        return isFirstYear;
    }
}
