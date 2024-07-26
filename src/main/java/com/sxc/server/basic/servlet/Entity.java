package com.sxc.server.basic.servlet;
/** <servlet>
 <servlet-name>login</servlet-name>
 <servlet-class>com.shsxt.LoginServlet</servlet-class>
 </servlet>*/
public class Entity {
    private String name;
    private String clz;
    public Entity() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public String getName() {
        return name;
    }

    public String getClz() {
        return clz;
    }
}
