package matteo.KillEnchant.registries;

import matteo.KillEnchant.EnchantLevelUpItem;
import matteo.KillEnchant.KillEnchant;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class ModRegistries {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, KillEnchant.MOD_ID);

    public static final Supplier<Item> enchant_levelup = registerItem("enchant_levelup", () -> new EnchantLevelUpItem((new Item.Properties()).durability(10).rarity(Rarity.RARE).setId(ResourceKey.create(ITEMS.getRegistryKey(), ResourceLocation.fromNamespaceAndPath(KillEnchant.MOD_ID, "enchant_levelup")))));

    public static Supplier<Item> registerItem(final String identifier, final Supplier<Item> supplier) {
        Supplier<Item> item = ITEMS.register(identifier, supplier);
        return item;
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
