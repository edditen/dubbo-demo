package com.tenchael.dubbo.plugin.utils;

import com.tenchael.dubbo.plugin.jmx.MBean;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;

public class NameUtils {

    public static String fullMethodName(Invoker<?> invoker, Invocation invocation) {
        return name(invoker.getInterface().getName(), invocation.getMethodName());
    }

    public static String name(String prefix, char split, String... names) {
        final StringBuilder builder = new StringBuilder();
        append(builder, split, prefix);
        if (names != null) {
            for (String s : names) {
                append(builder, split, s);
            }
        }
        return builder.toString();
    }

    public static String name(String prefix, String... names) {
        return name(prefix, '#', names);
    }

    private static void append(StringBuilder builder, char split, String part) {
        if (part != null && !part.isEmpty()) {
            if (builder.length() > 0) {
                builder.append(split);
            }
            builder.append(part);
        }
    }

    public static String baseOName(MBean mBean) {
        Class<? extends MBean> clazz = mBean.getClass();
        String packageName = clazz.getPackage().getName();
        String clazzName = clazz.getSimpleName();
        return String.format("%s:type=%s,name=", packageName, clazzName);
    }

}
