package com.proxym.libraryapp.Person;

import com.proxym.libraryapp.member.Member;

public class Resident extends Member {

    @Override
    public void payBook(int numberOfDays) {
        float price = 0.0f;
        if (numberOfDays <= 60) {
            price = numberOfDays * 0.1f;
        } else {
            int lateDays = numberOfDays - 60;
            price = (60 * 0.1f) + (lateDays * 0.2f);
        }
        setWallet(getWallet() - price);
     }

    public Resident(float initialWallet) {
        setWallet(initialWallet);
     }
    }

