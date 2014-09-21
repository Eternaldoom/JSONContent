package com.eternaldoom.jsoncontent;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class RealBlock extends Block{

	public RealBlock() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

}
