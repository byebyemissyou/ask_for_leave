package com.kade.lyx.ask_for_leave.entity;

/**
 * Created by zh on 2017/7/19.
 */

public class TypeBean {
    private String name;
    private boolean isSelect;

    public TypeBean(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
