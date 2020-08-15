package com.petrichor.rpc;

import lombok.experimental.PackagePrivate;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author petrichor
 * @date 2020/8/13 15:41
 */
public class ReflectUtil {

    //获取程序入口，在这里扫描有service注解的服务
    public static String getStackTrace(){
        StackTraceElement[] stack = new Throwable().getStackTrace();
        String entrance = stack[stack.length-1].getClassName();
        return entrance;
    }

    //getClasses 方法，传入一个包名，用于扫描该包及其子包下所有的类，并将其 Class 对象放入一个 Set 中返回。
    public static Set<Class<?>> getClasses(String packageName){
        Set<Class<?>> classes = new LinkedHashSet<>();
        boolean recursive = true;
        //把全类名换成文件夹格式
        String packageDirName = packageName.replace(".","/");
        Enumeration<URL> dirs;
        try{
            //
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代
            while(dirs.hasMoreElements()){
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议名
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if("file".equals(protocol)){
                    //获取包路径
                    String filePath = URLDecoder.decode(url.getFile(),"UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath,
                            recursive, classes);
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //某个路径下扫描出的一堆带@service的类
        return classes;
    }

    // 以文件的方式扫描整个包下的文件 并添加到集合中
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
                                                         boolean recursive, Set<Class<?>> classes) throws ClassNotFoundException {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        if(!dir.exists()||!dir.isDirectory()){
            return;
        }

        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive&&file.isDirectory())
                        ||(file.getName().endsWith(".class"));
            }
        });

        //循环所有文件
        for(File file:dirfiles){
            //如果是目录，递归扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                //如果是类文件，去掉.class只留下类名
                String className = file.getName().replace(".class","");
                classes.add(Class.forName(packageName + '.' + className));
            }

        }



    }


}

