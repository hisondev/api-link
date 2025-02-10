package io.github.hison.api.controller;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import io.github.hison.data.wrapper.DataWrapper;

/**
 * 
 * Utility class for working with MethodHandles in a flexible and dynamic manner.
 * This class provides methods to obtain method handles for specified methods on given classes,
 * facilitating dynamic method invocation without the need for reflection.
 * 
 * <p>Key Methods:</p>
 * <ul>
 * <li>{@code getMethodHandle}: Retrieves a method handle for a specified method with defined return type and parameter types.</li>
 * <li>{@code getFlexibleMethodHandle}: Attempts to find a method handle with various signatures, accommodating different return and parameter types.</li>
 * </ul>
 * <p>Usage Example:</p>
 * <pre>
 *    MethodHandle handle = MethodHandleUtil.getMethodHandle(MyClass.class, "myMethod", void.class, MyParam.class);
 *    handle.invokeExact(myClassInstance, myParamInstance);
 * </pre>
 * 
 * This class is particularly useful for scenarios where method calls need to be highly dynamic and determined at runtime.
 * Note: The usage of MethodHandles requires understanding of Java's MethodHandle API and should be used with caution.
 * 
 * @author Hani son
 * @version 1.0.1
 */
public class MethodHandleUtil {
    public static MethodHandle getMethodHandle(Class<?> clazz, String methodName, Class<?> returnType, Class<?>... parameterTypes) throws NoSuchMethodException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(returnType, parameterTypes);
        return lookup.findVirtual(clazz, methodName, methodType);
    }

    public static MethodHandle getFlexibleMethodHandle(Class<?> clazz, String methodName, Object serviceInstance) throws NoSuchMethodException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            MethodType methodType = MethodType.methodType(void.class);
            return lookup.findVirtual(clazz, methodName, methodType).bindTo(serviceInstance);
        } catch (NoSuchMethodException e) {}
        try {
            MethodType methodType = MethodType.methodType(DataWrapper.class);
            return lookup.findVirtual(clazz, methodName, methodType).bindTo(serviceInstance);
        } catch (NoSuchMethodException ex) {}
        try {
            MethodType methodType = MethodType.methodType(void.class, DataWrapper.class);
            return lookup.findVirtual(clazz, methodName, methodType).bindTo(serviceInstance);
        } catch (NoSuchMethodException e) {}
        try {
            MethodType methodType = MethodType.methodType(DataWrapper.class, DataWrapper.class);
            return lookup.findVirtual(clazz, methodName, methodType).bindTo(serviceInstance);
        } catch (NoSuchMethodException exc) {}
        throw new NoSuchMethodException("Method not found: " + methodName);
    }
}
