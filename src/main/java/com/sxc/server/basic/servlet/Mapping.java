package com.sxc.server.basic.servlet;

import java.util.HashSet;
import java.util.Set;

/**<servlet-mapping>
 <servlet-name>login</servlet-name>
 <url-pattern>/login</url-pattern>
 <url-pattern>/g</url-pattern>
 </servlet-mapping>*/
public class Mapping {
    private String name;
    private Set<String> patterns;
    public Mapping() {
        patterns = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatterns(Set<String> patterns) {
        this.patterns = patterns;
    }

    public String getName() {
        return name;
    }

    public Set<String> getPatterns() {
        return patterns;
    }

    public void addPatten(String pattern) {
        patterns.add(pattern);
    }
}
