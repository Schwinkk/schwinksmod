package com.schwink.schwinkmod.common;

import com.schwink.schwinkmod.common.AiGoals.ZombieFarmer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ZombieFarmerInitializer {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Config.MODID);

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    public static final Supplier<EntityType<ZombieFarmer>> ZOMBIE_FARMER = ENTITY_TYPES.register("zombie_farmer", () -> {
        ResourceKey<EntityType<?>> key = ResourceKey.create(
                Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Config.MODID, "zombie_farmer")
        );

        return EntityType.Builder.of(ZombieFarmer::new, MobCategory.MONSTER)
                .sized(0.6f, 1.95f)
                .build(key);
    });
}