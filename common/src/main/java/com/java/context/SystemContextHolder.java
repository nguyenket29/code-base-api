package com.java.context;

import java.util.List;

public class SystemContextHolder {

    private static ThreadLocal<SystemContext> instant = new ThreadLocal<>();

    public static void create(List<String> paths, String userName, String fullName, String userId, String email) {
        SystemContext holder = new SystemContext();
        holder.setPaths(paths);
        holder.setUserName(userName);
        holder.setFullName(fullName);
        holder.setUserId(userId);
        holder.setEmail(email);
        instant.set(holder);
    }


    public static ThreadLocal<SystemContext> getCurrentSystem() {
        return instant;
    }

    public static List<String> getCurrentPaths() {
        SystemContext context = getCurrentSystem().get();
        if (context != null) {
            return context.getPaths();
        }
        return null;
    }

    public static String getCurrentEmail() {
        SystemContext context = getCurrentSystem().get();
        if (context != null) {
            return context.getEmail();
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
