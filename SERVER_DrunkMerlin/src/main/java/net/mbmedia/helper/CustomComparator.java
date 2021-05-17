package net.mbmedia.helper;

import net.mbmedia.db.entities.FreundeMitPromille;

import java.util.Comparator;

public class CustomComparator implements Comparator<FreundeMitPromille> {


    @Override
    public int compare(FreundeMitPromille o1, FreundeMitPromille o2) {
        Double d1 = Double.valueOf(o1.getPromille());
        Double d2 = Double.valueOf(o2.getPromille());
        return d1.compareTo(d2);
    }
}
