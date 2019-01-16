package com.example.gaayathri.a6eskills;

import java.io.Serializable;
import java.util.List;

public class Skills implements Serializable {

    String name;
    Integer id;
    List<Skills> child;
    Boolean isSelectable;
    Integer selectedSubCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Skills> getChild() {
        return child;
    }

    public void setChild(List<Skills> child) {
        this.child = child;
    }

    public Boolean getSelectable() {
        return isSelectable;
    }

    public void setSelectable(Boolean selectable) {
        isSelectable = selectable;
    }

    public Integer getSelectedSubCount() {
        return selectedSubCount;
    }

    public void setSelectedSubCount(Integer selectedSubCount) {
        this.selectedSubCount = selectedSubCount;
    }
}
