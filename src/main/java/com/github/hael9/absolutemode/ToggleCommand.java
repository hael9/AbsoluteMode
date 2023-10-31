package com.github.hael9.absolutemode;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.util.Locale;

public class ToggleCommand extends CommandBase implements ICommand {

    @Override
    public String getCommandName() {
        return "tabletmode";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tabletmode toggle/sens";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Use /tabletmode toggle/sens"));
            return;
        }
        if (args[0].toLowerCase(Locale.ROOT).contains("toggle")) AbsoluteMode.toggled = !AbsoluteMode.toggled;
        if (args[0].toLowerCase(Locale.ROOT).contains("sens")) {
            if (args.length < 2) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Specify a sens"));
                return;
            }
            try {
                AbsoluteMode.sensitivity = Double.parseDouble(args[1]);
            } catch (Exception e) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Could not parse value"));
            }
        }

        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Command processed, updating config!"));
        try {
            AbsoluteMode.updateConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


