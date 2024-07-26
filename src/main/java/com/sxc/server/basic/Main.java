package com.sxc.server.basic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public class Iphone {
        private void hello() {
            System.out.println("我是iPhone");
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Class.forName(包名.类名)
        Class<?> clazz = Class.forName("com.sxc.server.basic.Main$Iphone");
        Iphone iphone = (Iphone) clazz.getConstructor(Main.class).newInstance(new Main());
        System.out.println(iphone);
        Method helloMethod = clazz.getDeclaredMethod("hello");
        helloMethod.invoke(iphone);
    }
}