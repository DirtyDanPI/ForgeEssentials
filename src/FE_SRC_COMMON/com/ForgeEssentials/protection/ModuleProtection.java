package com.ForgeEssentials.protection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.ForgeEssentials.core.IFEModule;
import com.ForgeEssentials.permission.PermissionRegistrationEvent;
import com.ForgeEssentials.permission.PermissionsAPI;
import com.ForgeEssentials.permission.Zone;
import com.ForgeEssentials.permission.ZoneManager;
import com.ForgeEssentials.permission.query.PermQuery;
import com.ForgeEssentials.permission.query.PermQueryPlayerZone;
import com.ForgeEssentials.util.OutputHandler;
import com.ForgeEssentials.util.AreaSelector.Point;
import com.ForgeEssentials.util.AreaSelector.WorldPoint;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

/**
 * @author Dries007
 */

public class ModuleProtection implements IFEModule
{
	public final static String PERM_EDITS = "ForgeEssentials.Protection.allowEdits";
	public final static String PERM_INTERACT_BLOCK = "ForgeEssentials.Protection.allowBlockInteractions";
	public final static String PERM_INTERACT_ENTITY = "ForgeEssentials.Protection.allowEntityInteractions";
	public final static String PERM_OVERRIDE = "ForgeEssentials.Protection.overrideProtection";
	
	public static ConfigProtection config;
	public static boolean enable = false;
	
	public static HashMap<String, HashMap<String, Boolean>> permissions = new HashMap<String, HashMap<String, Boolean>>();
	
	public ModuleProtection()
	{
		MinecraftForge.EVENT_BUS.register(this);
		
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(PermissionsAPI.GROUP_DEFAULT, false); map.put(PermissionsAPI.GROUP_MEMBERS, true); map.put(PermissionsAPI.GROUP_ZONE_ADMINS, true); map.put(PermissionsAPI.GROUP_OWNERS, true); 
		permissions.put(PERM_EDITS, map);
		permissions.put(PERM_INTERACT_BLOCK, map);
		permissions.put(PERM_INTERACT_ENTITY, map);
		
		map.put(PermissionsAPI.GROUP_MEMBERS, false);
		permissions.put(PERM_OVERRIDE, map);
	}

	/*
	 * Module part
	 */
	
	@Override
	public void preLoad(FMLPreInitializationEvent e)
	{
		if(!FMLCommonHandler.instance().getEffectiveSide().isServer()) return;
		config = new ConfigProtection();
		if(!enable) return;
		OutputHandler.SOP("Protection module is enabled. Loading...");
	}

	@Override
	public void load(FMLInitializationEvent e)
	{
		if(!enable) return;
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@Override
	public void postLoad(FMLPostInitializationEvent e){}

	@Override
	public void serverStopping(FMLServerStoppingEvent e){}
	
	@Override
	public void serverStarting(FMLServerStartingEvent e){}

	@Override
	public void serverStarted(FMLServerStartedEvent e){}

	@ForgeSubscribe
	public void registerPermissions(PermissionRegistrationEvent event)
	{
		//event.registerPermissionDefault(PERM, false);
		for(String perm : permissions.keySet())
		{
			event.registerGlobalGroupPermissions(PermissionsAPI.GROUP_GUESTS, 		perm, permissions.get(perm).get(PermissionsAPI.GROUP_DEFAULT));
			event.registerGlobalGroupPermissions(PermissionsAPI.GROUP_MEMBERS, 		perm, permissions.get(perm).get(PermissionsAPI.GROUP_MEMBERS));
			event.registerGlobalGroupPermissions(PermissionsAPI.GROUP_ZONE_ADMINS, 	perm, permissions.get(perm).get(PermissionsAPI.GROUP_ZONE_ADMINS));
			event.registerGlobalGroupPermissions(PermissionsAPI.GROUP_OWNERS, 		perm, permissions.get(perm).get(PermissionsAPI.GROUP_DEFAULT));
		}
	}
}