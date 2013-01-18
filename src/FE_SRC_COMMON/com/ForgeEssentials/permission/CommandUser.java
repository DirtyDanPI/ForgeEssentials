package com.ForgeEssentials.permission;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.ForgeEssentials.core.PlayerInfo;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.Localization;
import com.ForgeEssentials.util.OutputHandler;

public class CommandUser
{
	public static void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		if (args.length == 0) // display syntax & possible options for this level
		{
			//OutputHandler.chatError(sender, Localization.get(Localization.ERROR_BADSYNTAX) + "");
			return;
		}
		
		EntityPlayerMP player = FunctionHelper.getPlayerFromUsername(args[0]);
		if (player == null)
		{
			OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NOPLAYER, args[0]));
			OutputHandler.chatConfirmation(sender, args[0] + " will be used, but may be inaccurate.");
		}
		if (args.length == 1) // display user-specific settings & there values for this player
		{
			
			return;
		}
		else if (args[1].equalsIgnoreCase("supers")) // super perms management
		{
			if (args.length == 2) // display user super perms
			{
				return;
			}
			else if (args.length >= 3) // changing super perms
			{
				Zone zone = ZoneManager.SUPER;
				if (args.length == 5) // zone is set
				{
					if(ZoneManager.doesZoneExist(args[4]))
					{
						zone = ZoneManager.getZone(args[4]);
					}
					else
					{
						OutputHandler.chatError(sender, Localization.format(Localization.ERROR_ZONE_NOZONE, args[4]));
					}
				}
				
				if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("allow"))
				{
					PermissionsAPI.setPlayerPermission(player.username, args[3], true, zone.getZoneName());
					OutputHandler.chatConfirmation(sender, player.username + " has been allowed " + args[3]);
					return;
				}
				else if (args[2].equalsIgnoreCase("clear") || args[2].equalsIgnoreCase("remove")) // remove super
																									// perm settings
				{
					PermissionsAPI.clearPlayerPermission(player.username, args[3], zone.getZoneName());
					OutputHandler.chatConfirmation(sender, player.username + "'s access to " + args[2] + " cleared");
					return;
				}
				else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("deny")) // deny super perm
				{
					PermissionsAPI.setPlayerPermission(player.username, args[3], false, zone.getZoneName());
					OutputHandler.chatConfirmation(sender, player.username + " has been denied " + args[3]);
					return;
				}
				else if (args[2].equalsIgnoreCase("get"))
				{
					//Get current state.
					return;
				}
				
			}
		}
		else if (args[1].equalsIgnoreCase("group")) // group management
		{
			String zoneName = ZoneManager.GLOBAL.getZoneName();
			if (args.length == 5) // zone is set
			{
				if(ZoneManager.getZone(args[4]) != null)
				{
					zoneName = args[4];
				}
				else
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_ZONE_NOZONE, args[4]));
					return;
				}
			}
			
			if (args[2].equalsIgnoreCase("add")) // add player to group
			{
				if(args.length > 3)
				{
					String result = PermissionsAPI.addPlayerToGroup(args[3], player.getCommandSenderName(), zoneName);
					if(result != null)
					{
						OutputHandler.chatError(sender, result);
					}
					else
					{
						OutputHandler.chatConfirmation(sender, player.getCommandSenderName() + " added to group " + args[3]);
					}
				}
				else
				{
					OutputHandler.chatError(sender, Localization.get(Localization.ERROR_BADSYNTAX));
				}
				return;
			}
			else if (args[2].equalsIgnoreCase("remove")) // remove player from group
			{
				if(args.length > 3)
				{
					String result = PermissionsAPI.clearPlayerGroup(args[3], player.getCommandSenderName(), zoneName);
					if(result != null)
					{
						OutputHandler.chatError(sender, result);
					}
					else
					{
						OutputHandler.chatConfirmation(sender, player.getCommandSenderName() + " removed from group " + args[3]);
					}
				}
				return;
			}
			else if (args[2].equalsIgnoreCase("set")) // set player's group
			{
				if(args.length > 3)
				{
					String result = PermissionsAPI.setPlayerGroup(args[3], player.getCommandSenderName(), zoneName);
					if(result != null)
					{
						OutputHandler.chatError(sender, result);
					}
					else
					{
						OutputHandler.chatConfirmation(sender, player.getCommandSenderName() + "'s group set to " + args[3]);
					}
				}
				return;
			}
		}
		else if (args.length >= 3) // player management
		{
			String zoneName = ZoneManager.GLOBAL.getZoneName();
			if (args.length == 4) // zone is set
			{
				if(ZoneManager.getZone(args[3]) != null)
				{
					zoneName = args[3];
				}
				else
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_ZONE_NOZONE, args[4]));
					return;
				}
			}
			
			if (args[1].equalsIgnoreCase("prefix")) // prefix
			{
				if(args.length == 2)
				{
					OutputHandler.chatConfirmation(sender, player.username + "'s prefix is &f" + PlayerInfo.getPlayerInfo(player.username).prefix);
					return;
				}
				PlayerInfo.getPlayerInfo(player.username).prefix = args[2];
				OutputHandler.chatConfirmation(sender, player.username + "'s prefix set to &f" + args[2]);
				return;
			}
			else if (args[1].equalsIgnoreCase("suffix")) // suffix
			{
				if(args.length == 2)
				{
					OutputHandler.chatConfirmation(sender, player.username + "'s suffix is &f" + PlayerInfo.getPlayerInfo(player.username).suffix);
					return;
				}
				PlayerInfo.getPlayerInfo(player.username).suffix = args[2];
				OutputHandler.chatConfirmation(sender, player.username + "'s suffix set to &f" + args[2]);
				return;
			}
			else if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("allow")) // allow player perm
			{
				String result = PermissionsAPI.setPlayerPermission(player.getCommandSenderName(), args[2], true, zoneName);
				if(result != null)
				{
					OutputHandler.chatError(sender, result);
				}
				else
				{
					OutputHandler.chatConfirmation(sender, player.username + "  allowed access to " + args[2] + ".");
				}
				return;
			}
			else if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("remove")) // remove perm settings
			{
				String result = PermissionsAPI.clearPlayerPermission(player.getCommandSenderName(), args[2], zoneName);
				if(result != null)
				{
					OutputHandler.chatError(sender, result);
				}
				else
				{
					OutputHandler.chatConfirmation(sender, player.username + " denied access to " + args[2] + ".");
				}
				return;
			}
			else if (args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("deny")) // deny player perm
			{
				String result = PermissionsAPI.setPlayerPermission(player.getCommandSenderName(), args[2], false, zoneName);
				if(result != null)
				{
					OutputHandler.chatError(sender, result);
				}
				else
				{
					OutputHandler.chatConfirmation(sender, player.username + "'s  access to " + args[2] + "cleared.");
				}
				return;
			}
		}
		
		OutputHandler.chatError(sender, Localization.get(Localization.ERROR_BADSYNTAX) + "");
	}

	public static void processCommandConsole(ICommandSender sender, String[] args)
	{
		// Copy paste :p
	}

}
