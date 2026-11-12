package com.dragonomous.fartteleports;

import java.util.Arrays;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FartTeleportsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FartTeleportsPlugin.class);
		String[] devArgs = Arrays.copyOf(args, args.length + 1);
		devArgs[args.length] = "--developer-mode";
		RuneLite.main(devArgs);
	}
}
