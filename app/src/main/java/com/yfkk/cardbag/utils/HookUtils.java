package com.yfkk.cardbag.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookUtils {


    public static Method findMethodExact(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method=clazz.getDeclaredMethod(methodName,parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            return null;
        }
    }
    public static Field findFieLd(Class<?> clazz, String fieldName)
    {
        Field fid=null;
        try {
            fid=clazz.getDeclaredField(fieldName);
        }catch (Exception e){
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null || clazz.equals(Object.class))
                    break;

                try {
                    fid=clazz.getDeclaredField(fieldName);

                } catch (Exception ignored) {}
            }
        }
        return  fid;
    }

    public static void HookJavaMethod(Class<?> clazz,String methodName,Class<?>... parameterTypes)
    {
        Method hookMethod=findMethodExact(clazz,methodName,parameterTypes);//获取反射方法

        Class<?> declaringClass=hookMethod.getDeclaringClass();
        try{
            Field fid=findFieLd(hookMethod.getClass(),"slot");//获取slot字段
            fid.setAccessible(true);
        }catch (Exception e)
        {
            String msg=e.getMessage();
            return;
        }
//        hookMethodNative(declaringClass,slot);//进入Native层Hook
        int a=0;
    }


//    public static void replaceMethod(Activity activity){
//        //通过Activity.class 拿到 mInstrumentation字段
//
//        Field field = Activity.class.getDeclaredField("mInstrumentation");
//        field.setAccessible(true);
//        //根据activity内mInstrumentation字段 获取Instrumentation对象
//        Instrumentation instrumentation = (Instrumentation) field.get(activity);
//        //创建代理对象
//        Instrumentation instrumentationProxy = new ActivityProxyInstrumentation(instrumentation);
//        //进行替换
//        field.set(activity, instrumentationProxy);
//    }


//    public static void hookOnClickListener(View view) throws Exception {
//        // 第一步：反射得到 ListenerInfo 对象
//        Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
//        getListenerInfo.setAccessible(true);
//        Object listenerInfo = getListenerInfo.invoke(view);
//        // 第二步：得到原始的 OnClickListener事件方法
//        Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
//        Field mOnClickListener = listenerInfoClz.getDeclaredField("mOnClickListener");
//        mOnClickListener.setAccessible(true);
//        View.OnClickListener originOnClickListener = (View.OnClickListener) mOnClickListener.get(listenerInfo);
//        // 第三步：用 Hook代理类 替换原始的 OnClickListener
//        View.OnClickListener hookedOnClickListener = new HookedClickListenerProxy(originOnClickListener);
//        mOnClickListener.set(listenerInfo, hookedOnClickListener);
//    }

}
