package com.github.hlam.types;

import java.io.Serializable;
import java.util.Objects;

public class Pojo implements Serializable {
    private int intVal;
    private String strVal;

    public Pojo() {
    }

    public Pojo(int intVal, String strVal) {
        this.intVal = intVal;
        this.strVal = strVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pojo pojo = (Pojo) o;
        return intVal == pojo.intVal && Objects.equals(strVal, pojo.strVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intVal, strVal);
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "intVal=" + intVal +
                ", strVal='" + strVal + '\'' +
                '}';
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }
}
