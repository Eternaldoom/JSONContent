package com.eternaldoom.jsoncontent;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ISpecialArmor;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemJSONArmor extends ItemArmor implements ISpecialArmor {

	protected String texture;
	protected int piece;
	private Double reduction;
	private String pack;

	public ItemJSONArmor(int piece, String tex, double reduction, int uses, String contentPack) {
		super(ArmorMaterial.CHAIN, 3, piece);
		setMaxDamage(uses);
		this.texture = tex;
		this.reduction = reduction;
		this.pack = contentPack;
		this.piece = piece;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot,
			String type) {
			if (slot == 2) {
				return this.pack + ":textures/armor/" + this.texture
						+ "_2.png";
			} else {
				return this.pack + ":textures/armor/" + this.texture
						+ "_1.png";
			}
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player,
			ItemStack armor, DamageSource source, double damage, int slot) {
		return new ISpecialArmor.ArmorProperties(0, reduction / 100,
				50000);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return slot = (int) Math.round((reduction * 100) / 4);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		if (!source.isFireDamage() && !source.isMagicDamage())
			stack.damageItem(1, entity);
	}
}