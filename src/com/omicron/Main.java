package com.omicron;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        File file = new File("./");
        File mods = new File(file, "mods");
        File resourcepacks = new File(file, "resourcepacks");

        if(mods.exists())
            for(File jar : mods.listFiles())
                if(jar.getName().endsWith(".jar"))
                    unJar(jar, new File("./scraping/mods/" + jar.getName()));
        if(resourcepacks.exists())
            for(File zip : resourcepacks.listFiles())
                if(zip.getName().endsWith(".zip"))
                    unJar(zip, new File("./scraping/resourcepacks/" + zip.getName()));
    }

    public static void unZip(File archive, File destDir) throws IOException {
        byte[] buffer = new byte[256 * 1024];
        try (ZipFile zip = new JarFile(archive)) {
            String modId = "";
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry ent = entries.nextElement();
                if (ent.getName().matches("assets/.*") && ent.getName().split("/").length >= 2) {
                    modId = ent.getName().split("/")[1];
                    if (zip.getEntry("assets/" + modId) == null)
                        new File(destDir, modId).mkdir();
                }
                if(ent.getName().matches("assets/.*/textures/.*"))
                {
                     File f = new File(destDir + "/" + modId, ent.getName().replaceAll("assets/.*/textures/", ""));

                    if (!ent.isDirectory()) {
                        f.getParentFile().mkdirs();
                    }
                    else
                    {
                        f.mkdirs();
                        continue;
                    }
                    try (InputStream is = zip.getInputStream(ent);
                         FileOutputStream os = new FileOutputStream(f)) {
                        destDir.mkdirs();
                        for (int r; (r = is.read(buffer)) > 0; ) {
                            os.write(buffer, 0, r);
                        }
                    }

                }
            }
        }
    }

    public static void unJar(File archive, File destDir) throws IOException {
        byte[] buffer = new byte[256 * 1024];
        try (JarFile jar = new JarFile(archive)) {
            String modId = "";
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry ent = entries.nextElement();
                if (ent.getName().matches("assets/.*") && ent.getName().split("/").length >= 2) {
                    modId = ent.getName().split("/")[1];
                    if (jar.getEntry("assets/" + modId) == null)
                        new File(destDir, modId).mkdir();
                }
                if(ent.getName().matches("assets/.*/textures/.*"))
                {
                    File f = new File(destDir + "/" + modId, ent.getName().replaceAll("assets/.*/textures/", ""));

                    if (!ent.isDirectory()) {
                        f.getParentFile().mkdirs();
                    }
                    else
                    {
                        f.mkdirs();
                        continue;
                    }
                    try (InputStream is = jar.getInputStream(ent);
                         FileOutputStream os = new FileOutputStream(f)) {
                        destDir.mkdirs();
                        for (int r; (r = is.read(buffer)) > 0; ) {
                            os.write(buffer, 0, r);
                        }
                    }

                }
            }
        }
    }
}
