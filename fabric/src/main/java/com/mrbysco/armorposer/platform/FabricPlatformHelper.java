package com.mrbysco.armorposer.platform;

import com.mrbysco.armorposer.config.PoserConfig;
import com.mrbysco.armorposer.data.SwapData;
import com.mrbysco.armorposer.data.SyncData;
import com.mrbysco.armorposer.packets.ArmorStandSwapPayload;
import com.mrbysco.armorposer.packets.ArmorStandSyncPayload;
import com.mrbysco.armorposer.platform.services.IPlatformHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {
	@Override
	public void updateEntity(ArmorStand armorStand, CompoundTag compound) {
		CompoundTag CompoundNBT = armorStand.saveWithoutId(new CompoundTag()).copy();
		CompoundNBT.merge(compound);
		armorStand.load(CompoundNBT);

		SyncData data = new SyncData(armorStand.getUUID(), compound);
		ClientPlayNetworking.send(new ArmorStandSyncPayload(data));
	}

	@Override
	public void swapSlots(ArmorStand armorStand, SwapData.Action action) {
		SwapData data = new SwapData(armorStand.getUUID(), SwapData.Action.SWAP_WITH_HEAD);
		ClientPlayNetworking.send(new ArmorStandSwapPayload(data));
	}

	@Override
	public boolean allowScrolling() {
		PoserConfig config = AutoConfig.getConfigHolder(PoserConfig.class).getConfig();
		return config.general.allowScrolling;
	}

	@Override
	public Path getUserPresetFolder() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
