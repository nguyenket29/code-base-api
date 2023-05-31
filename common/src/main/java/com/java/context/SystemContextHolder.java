package com.java.context;

public class SystemContextHolder {

    private static ThreadLocal<SystemContext> instant = new ThreadLocal<>();

    public static void create(String path, String userName, String fullName, String userId) {
        SystemContext holder = new SystemContext();
        holder.setPath(path);
        holder.setUserName(userName);
        holder.setFullName(fullName);
        holder.setUserId(userId);
        instant.set(holder);
    }


    public static ThreadLocal<SystemContext> getCurrentSystem() {
        return instant;
    }

    public static String getCurrentPath() {
        SystemContext context = getCurrentSystem().get();
        if (context != null) {
            return context.getPath();
        }
        return null;
    }

    public static String getCurrentUserName() {
        SystemContext context = getCurrentSystem().get();
        if (context != null) {
            return context.getUserName();
        }
        return null;
    }

    public static String getCurrentUserId() {
        SystemContext context = getCurrentSystem().get();
        if (context != null) {
            return context.getUserId();
        }
        return null;
    }
}
