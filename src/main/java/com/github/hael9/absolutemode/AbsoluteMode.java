package com.github.hael9.absolutemode;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.commons.io.IOUtils;

import java.io.*;

@Mod(modid = "tabletmode", useMetadata=true)
public class AbsoluteMode {
    public static boolean toggled = true;
    public static int lastX = 0;
    public static int lastY = 0;
    public static double sensitivity = 1;
    public static File configFile;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new ToggleCommand());

        File configFolder = new File(Minecraft.getMinecraft().mcDataDir, "config");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        configFile = new File(configFolder, "absolutemode");
        if (!configFile.exists()) {
            updateConfig();
        }

        loadConfig();
    }

    public static void updateConfig() throws IOException {
        String str = sensitivity + "," + toggled;
        FileOutputStream fos = new FileOutputStream(configFile);
        IOUtils.write(str, fos);
        fos.close();
    }

    public static void loadConfig() throws IOException {
        FileInputStream fis = new FileInputStream(configFile);
        String str = IOUtils.toString(fis);
        String[] line = str.split(",");
        sensitivity = Double.parseDouble(line[0]);
        toggled = Boolean.parseBoolean(line[1]);
        fis.close();
    }
}
