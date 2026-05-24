package com.jonhu.core.styles;

/**
 * 存储一种类型
 */
public class Type {
    private String model;

    private String description;

    public Type(String model, String description) {
        this.model = model;
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 只比较model即可认定Type相同
     */
    public boolean equals(Type type) {
        return model.equals(type.model);
    }
}
