package com.cjh.spring.mvcframework.v2.servlet;

import com.cjh.spring.mvcframework.annotation.MyAutowired;
import com.cjh.spring.mvcframework.annotation.MyController;
import com.cjh.spring.mvcframework.annotation.MyRequestMapping;
import com.cjh.spring.mvcframework.annotation.MyService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author cjh
 * @date 2020/2/2 13:50
 **/
@Log4j2
public class MyMvcServlet extends HttpServlet {

    private Properties configProperties = new Properties();
    private List<String> classNames = new ArrayList<>();
    private Map<String, Object> ioc = new HashMap<>();
    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1、初始化配置
        doInitConfig(config.getInitParameter("contextConfigLocation"));
        // 2、扫描包下的类
        doScanner(configProperties.getProperty("contextConfigLocation"));
        // 2、对象实例化，初始化IOC容器，将需要实例化的对象放到ioc容器中。
        doInstance();
        // 3、DI依赖注入
        doAutowired();
        // 4、创建HelperMapping
        createHelperMapping();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception,Detail ....\n" + Arrays.toString(e.getStackTrace()));
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IllegalAccessException, IOException, InvocationTargetException {
        try {
            String url = req.getPathInfo();
            HandlerMapping handlerMapping = getHandlerMapping(url);
            if (handlerMapping == null) {
                resp.getWriter().write("404 Con Not Find Page!!!");
                return;
            }
            Method method = handlerMapping.getMethod();
            Object[] params = new Object[handlerMapping.getParameterTypes().length];
            Map<String,String[]> reqParams = req.getParameterMap();
            // 构造入参
            for (Map.Entry<String, Integer> entry : handlerMapping.getParamIndexMapping().entrySet()) {
                if (reqParams.get(entry.getKey()) == null || reqParams.get(entry.getKey()).length == 0) {
                    resp.getWriter().write("500 Params is not match!!!");
                    return;
                }
                String value = Arrays.toString(reqParams.get(entry.getKey())).replaceAll("\\[|\\]|,|\\s","");
                params[entry.getValue()] = value;
            }
            Object result = method.invoke(handlerMapping.getController(), params);
            resp.getWriter().write(result.toString());
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 匹配url对应的handlerMapping
     *
     * @param url
     * @return
     */
    private HandlerMapping getHandlerMapping(String url) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.getUrl().equals(url)) {
                return handlerMapping;
            }
        }
        return null;
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        try {
            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
                Field[] fields = entry.getValue().getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MyAutowired.class)) {
                        field.setAccessible(true);
                        MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
                        String beanName = myAutowired.value();
                        if (!"".equals(beanName.trim())) {
                            assertNotNull(beanName);
                            field.set(entry.getValue(), ioc.get(beanName));
                        } else {
                            assertNotNull(field.getType().getName());
                            field.set(entry.getValue(), ioc.get(field.getType().getName()));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void assertNotNull(String beanName) {
        if (ioc.get(beanName) == null) {
            throw new RuntimeException("Can not find <<" + beanName + ">> instance!!");
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {
                Class clazz = Class.forName(className);
                String beanName = toLowerFirstCase(clazz.getSimpleName());
                if (clazz.isAnnotationPresent(MyController.class)) {
                    ioc.put(beanName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    MyService myService = (MyService) clazz.getAnnotation(MyService.class);
                    if (!"".equals(myService.value().trim())) {
                        beanName = myService.value();
                    }

                    Object instance = clazz.newInstance();
                    ioc.put(beanName, clazz.newInstance());

                    // 根据实现的接口类型为每个接口创建一个对象
                    for (Class anInterface : clazz.getInterfaces()) {
                        ioc.put(anInterface.getName(), instance);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String path) {
        URL url = this.getClass().getResource("/" + path.replaceAll("\\.", "/"));
        File files = new File(url.getPath());
        for (File file : files.listFiles()) {
            if (file.isDirectory()) {
                doScanner(path + "." + file.getName());
            } else {
                if (file.getName().endsWith(".class")) {
                    classNames.add(path + "." + file.getName().replaceAll(".class", ""));
                }
            }
        }
    }

    private void createHelperMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class clazz = entry.getValue().getClass();
            if (clazz.isAnnotationPresent(MyController.class)) {
                String baseUrl = "";
                if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                    MyRequestMapping requestMapping = (MyRequestMapping) clazz.getAnnotation(MyRequestMapping.class);
                    baseUrl = requestMapping.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(MyRequestMapping.class)) {
                        String url = "/" + baseUrl + "/" + method.getAnnotation(MyRequestMapping.class).value();
                        url = url.replaceAll("/+", "/");
                        handlerMappings.add(new HandlerMapping(url, entry.getValue(), method));
                        System.out.println("Mapped >>>>> " + url);
                    }
                }
            }
        }
    }

    /**
     * 初始化配置
     *
     * @param contextConfigLocation 路径信息
     */
    private void doInitConfig(String contextConfigLocation) {
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            configProperties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
