package matteo.KillEnchant;

import matteo.KillEnchant.registries.Command;
import matteo.KillEnchant.registries.KillEnchantConfig;
import matteo.KillEnchant.registries.ModRegistries;

import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.util.*;


@Mod(KillEnchant.MOD_ID)
public class KillEnchant {

    public static final String MOD_ID = "killenchant";
    public static final Logger LOGGER = LogManager.getLogger("killenchant");

    public KillEnchant(IEventBus bus, ModContainer container) {
        NeoForge.EVENT_BUS.register(this);
        ModRegistries.register(bus);
        container.registerConfig(ModConfig.Type.SERVER, KillEnchantConfig.CONFIG_SPEC);

        LOGGER.info("KillEnchant initialized");
    }

    @SubscribeEvent
    public void serverLoad(RegisterCommandsEvent commands) {
        Command.command(commands.getDispatcher());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void enchantBeGone(EnchantmentLevelSetEvent event) {
        BlockPos pos = event.getPos();
        Level world = event.getLevel();
        Player player = (Player) findEntityInWorldRange(world, Player.class, pos.getX(), pos.getY(), pos.getZ(), 10);
        CompoundTag data = player.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
        int killEnchant = data.getInt("KillEnchant").orElse(30);


            if (event.getOriginalLevel() < 10) { //<= (killEnchant / 5) && event.getOriginalLevel() < 16) {
                event.getEnchantRow();
                event.setEnchantLevel(killEnchant / 5);
            }

            if (event.getOriginalLevel() < 20 && event.getOriginalLevel() > 10) { //<= (killEnchant / 2) && event.getOriginalLevel() >= (killEnchant / 5) && event.getOriginalLevel() < 38) {
                event.getEnchantRow();
                event.setEnchantLevel(killEnchant / 2);
            }

            if (event.getOriginalLevel() < 40 && event.getOriginalLevel() > 20) { //< killEnchant && event.getOriginalLevel() > (killEnchant / 2)) {
                event.getEnchantRow();
                event.setEnchantLevel(killEnchant);
            }
    }

    @SubscribeEvent
    public void murder(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player target && event.getSource().getEntity() instanceof Player killer) {
            if (target.getName() != killer.getName()) {
                if (target.gameMode().isSurvival() && killer.gameMode().isSurvival()) {
                    CompoundTag dataKiller = killer.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
                    CompoundTag dataTarget = target.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
                    int killerInt = dataKiller.getInt("KillEnchant").orElse(30);
                    int targetInt = dataTarget.getInt("KillEnchant").orElse(30);

                    double randomIncrease = (RandomSource.create().nextDouble() * (KillEnchantConfig.random_max_additional_increase.get() - KillEnchantConfig.random_min_additional_increase.get()) + KillEnchantConfig.random_min_additional_increase.get());
                    double randomDecrease = (RandomSource.create().nextDouble() * (KillEnchantConfig.random_max_additional_decrease.get() - KillEnchantConfig.random_min_additional_decrease.get()) + KillEnchantConfig.random_min_additional_decrease.get());

                    killerInt = killerInt + (int) (KillEnchantConfig.increase_per_kills.get() + randomIncrease);
                    targetInt = targetInt - (int) (KillEnchantConfig.decrease_per_deaths.get() + randomDecrease);
                    if (killerInt > KillEnchantConfig.max_level.get()) { killerInt = KillEnchantConfig.max_level.get(); }
                    if (targetInt < KillEnchantConfig.min_level.get()) { targetInt = KillEnchantConfig.min_level.get(); }

                    dataKiller.putInt("KillEnchant", killerInt);
                    dataTarget.putInt("KillEnchant", targetInt);

                    killer.getPersistentData().put(Player.PERSISTED_NBT_TAG, dataTarget);
                    target.getPersistentData().put(Player.PERSISTED_NBT_TAG, dataKiller);
                }
            }
        }
    }

    public static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
        return world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
    }
}