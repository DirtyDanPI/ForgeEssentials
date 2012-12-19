package com.ForgeEssentials.coremod;

/**
 * Kindly do not reference any FE classes outside the coremod package in this class.
 * This is a store room for all String[]s used by the coremod, 99% of stuff is edited here and not in the actual coremod classes.
 */

public class Data
{

	protected static String[]	libraries		= { "mysql-connector-java-bin.jar", "sqlite-jdbc.jar" };
	protected static String[]	checksums		= { "3ae0cff91d7f40d5b4c7cefbbd1eab34025bdc15", "cea9f7f8e6bcb580d953a8651fb8391640de0f85" };
	protected static String[]	transformers	= { "com.ForgeEssentials.coremod.FEPermissionsTransformer" };
    protected static String[]   downloaders     = {"com.ForgeEssentials.coremod.SQLDownloader", "com.ForgeEssentials.coremod.WEDownloader"};
}
