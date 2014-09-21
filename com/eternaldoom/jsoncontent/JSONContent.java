package com.eternaldoom.jsoncontent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    		File folder = new File("resourcepacks/jsoncontent/assets/jsoncontent/contentpacks");
    		File[] filelist = folder.listFiles();
    		for(int c = 0; c<filelist.length; c++){
    			if(filelist[c].getName().contains(".json")){
    				Reader reader = new InputStreamReader(new FileInputStream("resourcepacks/jsoncontent/assets/jsoncontent/contentpacks/" + filelist[c].getName()), "UTF-8");
    				Gson gson = new GsonBuilder().setPrettyPrinting().create();
    				JSONObject[] stufffields = gson.fromJson(reader, JSONObject[].class);
    				ArrayList<CreativeTabs> creativetabs = new ArrayList<CreativeTabs>();
	    	
    				for(int i = 0; i < stufffields.length; i++){
    					if(stufffields[i].type.equals("block")){
    						Block block = new RealBlock().setBlockName(stufffields[i].name).setBlockTextureName(stufffields[i].texture).setHardness((float)stufffields[i].hardness).setResistance((float)stufffields[i].resistance).setCreativeTab((CreativeTabs)creativetabs.toArray()[stufffields[i].tabid]);
    						GameRegistry.registerBlock(block, stufffields[i].id);
    					}else if(stufffields[i].type.equals("item")){
    						Item item = new RealItem().setUnlocalizedName(stufffields[i].name).setTextureName(stufffields[i].texture).setCreativeTab((CreativeTabs)creativetabs.toArray()[stufffields[i].tabid]);
    						GameRegistry.registerItem(item, stufffields[i].id);
    					}else if(stufffields[i].type.equals("creativetab")){
    						final Item thetabitem = GameRegistry.findItem("minecraft", stufffields[i].tabitem);
    						creativetabs.add(new CreativeTabs(CreativeTabs.getNextID(), stufffields[i].name){
    							@SideOnly(Side.CLIENT)
    							public Item getTabIconItem()
    							{
    								return thetabitem;
    							}
    						});
    					}
    				}
    			}
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}