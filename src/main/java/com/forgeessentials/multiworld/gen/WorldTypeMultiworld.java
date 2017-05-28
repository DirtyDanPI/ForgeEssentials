package com.forgeessentials.multiworld.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderOverworld;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerZoom;

import com.forgeessentials.multiworld.WorldServerMultiworld;

/**
 * 
 * @author Olee
 */
public class WorldTypeMultiworld extends WorldType
{

    private WorldServerMultiworld currentMultiworld;

    public WorldTypeMultiworld()
    {
        super("multiworld");
    }

    @Override
    public BiomeProvider getBiomeProvider(World world)
    {
        // Set current world
        if (world instanceof WorldServerMultiworld)
            currentMultiworld = (WorldServerMultiworld) world;
        else
            currentMultiworld = null;

        // TODO: Use custom WorldChunkManager to generate custom worlds
        if (this == FLAT)
        {
            FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(world.getWorldInfo().getGeneratorOptions());
            return new BiomeProviderSingle(Biome.getBiome(flatgeneratorinfo.getBiome()));
        }
        else
        {
            return new BiomeProvider(world.getWorldInfo());
        }
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
    {
        // TODO: Use custom ChunkProviders
        if (this == FLAT)
            return new ChunkProviderFlat(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
        if (this == DEBUG_WORLD)
            return new ChunkProviderDebug(world);
        if (this == CUSTOMIZED)
            return new ChunkProviderOverworld(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
        return new ChunkProviderOverworld(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
    }

    /**
     * Creates the GenLayerBiome used for generating the world
     *
     * @param worldSeed
     *            The world seed
     * @param parentLayer
     *            The parent layer to feed into any layer you return
     * @return A GenLayer that will return ints representing the Biomes to be generated, see GenLayerBiome
     */
    @Override
    public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, String chunkProviderSettingsJson)
    {
        // TODO: Temporary solution to allow changing basic biomes - but a customized WorldChunkManager would remove the
        // need for that
        if (currentMultiworld == null)
        {
            GenLayer ret = new GenLayerBiome(200L, parentLayer, this, chunkProviderSettingsJson);
            ret = GenLayerZoom.magnify(1000L, ret, 2);
            ret = new GenLayerBiomeEdge(1000L, ret);
            return ret;
        }
        else
        {
            GenLayer ret = new GenLayerMultiworldBiome(200L, parentLayer, currentMultiworld);
            ret = GenLayerZoom.magnify(1000L, ret, 2);
            ret = new GenLayerBiomeEdge(1000L, ret);
            return ret;
        }
    }

    // ============================================================
    // World options

    @Override
    public int getMinimumSpawnHeight(World world)
    {
        return this == FLAT ? 4 : 64;
    }

    @Override
    public double getHorizon(World world)
    {
        return this == FLAT ? 0.0D : 63.0D;
    }

    @Override
    public double voidFadeMagnitude()
    {
        return this == FLAT ? 1.0D : 0.03125D;
    }

    @Override
    public boolean handleSlimeSpawnReduction(Random random, World world)
    {
        return this == FLAT ? random.nextInt(4) != 1 : false;
    }

    /**
     * Get the height to render the clouds for this world type
     * 
     * @return The height to render clouds at
     */
    @Override
    public float getCloudHeight()
    {
        return 128.0F;
    }

}
