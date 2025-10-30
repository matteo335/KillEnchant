package matteo.KillEnchant;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;

import matteo.KillEnchant.registries.KillEnchantConfig;

public class EnchantLevelUpItem extends Item {

    public EnchantLevelUpItem(Properties properties) {
        super(properties);
    }

    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        InteractionResult result = super.use(world, player, hand);

        if (!world.isClientSide()) {
            ItemStack item = player.getItemInHand(hand).finishUsingItem(world, player);
            CompoundTag data = player.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
            int killEnchant = data.getInt("KillEnchant").orElse(30);

            if (killEnchant > (KillEnchantConfig.max_level.get() - KillEnchantConfig.enchant_levelup_item_increase.get())) { player.displayClientMessage(Component.literal("Too high!! your enchantment level is high enough, going higher would be useless"), false); }
            else {
                double randomIncrease = (RandomSource.create().nextDouble() * (KillEnchantConfig.random_additional_increase_levelup_item_max.get() - KillEnchantConfig.random_additional_increase_levelup_item_min.get()) + KillEnchantConfig.random_additional_increase_levelup_item_min.get());

                killEnchant = killEnchant + (int) (KillEnchantConfig.enchant_levelup_item_increase.get() + randomIncrease);

                data.putInt("KillEnchant", killEnchant);
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, data);
                player.displayClientMessage(Component.literal("Your EnchantLevel score is now " + killEnchant), false);
                item.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
        }
        return result;
    }
}
