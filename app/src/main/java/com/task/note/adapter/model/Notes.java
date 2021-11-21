package com.task.note.adapter.model;

import java.util.Comparator;

public class Notes {
    public static Comparator<Notes> titleComparator = new Comparator<Notes>() {
        @Override
        public int compare(Notes o1, Notes o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    private int id;
    private String title;
    private String Description;
    private Long modify;
    private String created;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getModify() {
        return modify;
    }

    public void setModify(Long modify) {
        this.modify = modify;
    }
}
