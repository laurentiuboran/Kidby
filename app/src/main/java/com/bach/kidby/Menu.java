//documented

package com.bach.kidby;

public class Menu {
    private String name;
    private String content;
    private Class c;

    public Menu() {}

    public Menu(String name, String content, Class c) {
        this.name = name;
        this.content = content;
        this.c = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Class getClassMenu() {
        return c;
    }

    public void setClassMenu(Class name) {
        this.c = c;
    }
}
