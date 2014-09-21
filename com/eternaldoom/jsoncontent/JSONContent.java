package com.eternaldoom.jsoncontent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = JSONContent.MODID, version = JSONContent.VERSION)
public class JSONContent
{
    public static final String MODID = "jsoncontent";
    public static final String VERSION = "Alpha 1.0";
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event){    	
    	try {
    		File folder = new File("contentpacks");
    		if(FMLCommonHandler.instance().getSide().isClient()) folder = new File("resourcepacks/jsoncontent/assets/jsoncontent/contentpacks");
    		File[] filelist = folder.listFiles();
    		for(int c = 0; c<filelist.length; c++){
    			if(filelist[c].getName().contains(".json")){
    				Reader reader = null;
    				if(FMLCommonHandler.instance().getSide().isServer()) reader = new InputStreamReader(new FileInputStream("contentpacks/" + filelist[c].getName()), "UTF-8");
    				if(FMLCommonHandler.instance().getSide().isClient()) reader = new InputStreamReader(new FileInputStream("resourcepacks/jsoncontent/assets/jsoncontent/contentpacks/" + filelist[c].getName()), "UTF-8");
    				Gson gson = new GsonBuilder().setPrettyPrinting().create();
    				ContentPack pack = gson.fromJson(reader, ContentPack.class);
    				ArrayList<CreativeTabs> creativetabs = new ArrayList<CreativeTabs>();
    				String[] tabItemIds = {};
    				ArrayList<Item> tabItems = new ArrayList<Item>();
    				LogManager.getLogger().info("Loading content pack " + pack.packId);
    				for(int i = 0; i < pack.content.length; i++){
    					if(pack.content[i].type.equals("block")){
    						Block block = new RealBlock().setBlockName(pack.content[i].name).setBlockTextureName(pack.content[i].texture).setHardness((float)pack.content[i].hardness).setResistance((float)pack.content[i].resistance).setCreativeTab((CreativeTabs)creativetabs.toArray()[pack.content[i].tabid]);
    						GameRegistry.registerBlock(block, pack.content[i].id);
    					}else if(pack.content[i].type.equals("item")){
    						Item item = new RealItem().setUnlocalizedName(pack.content[i].name).setTextureName(pack.content[i].texture).setCreativeTab((CreativeTabs)creativetabs.toArray()[pack.content[i].tabid]);
    						GameRegistry.registerItem(item, pack.content[i].id);
    						for(int o = 0; o<tabItemIds.length; o++){
    							if(pack.content[i].id == tabItemIds[o]){
    								tabItems.add(item);
    							}
    						}
    					}else if(pack.content[i].type.equals("creativetab")){
    						Item tabItem = null;
    						if(!pack.content[i].tabMod.equals("minecraft")){
    							for(int j = 0; j<tabItemIds.length; j++){
    								if(pack.content[i].tabitem == tabItemIds[j]) tabItem = (Item)tabItems.toArray()[j];
    							}
    						}else{
    							tabItem = GameRegistry.findItem("minecraft", pack.content[i].tabitem);
    						}
    						final Item thetabitem = tabItem;
    						
    						CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), pack.content[i].name){
    							@SideOnly(Side.CLIENT)
    							public Item getTabIconItem()
    							{
    								return thetabitem;
    							}
    						};
    						creativetabs.add(tab);
    					}
    				}
					LogManager.getLogger().info(pack.packId + " sucessfully loaded!");
    			}
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}