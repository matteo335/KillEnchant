package matteo.KillEnchant.registries;

import net.neoforged.neoforge.common.ModConfigSpec;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.tuple.Pair;

public class KillEnchantConfig {
    public static final KillEnchantConfig SERVER;
    public static final ModConfigSpec CONFIG_SPEC;
    public static final Logger LOGGER = LogManager.getLogger("KillEnchant");

    public static ModConfigSpec.ConfigValue<Integer> min_level;
    public static ModConfigSpec.ConfigValue<Integer> max_level;
    public static ModConfigSpec.ConfigValue<Integer> increase_per_kills;
    public static ModConfigSpec.ConfigValue<Integer> decrease_per_deaths;
    public static ModConfigSpec.ConfigValue<Integer> enchant_levelup_item_increase;

    public static ModConfigSpec.DoubleValue random_min_additional_increase;
    public static ModConfigSpec.DoubleValue random_max_additional_increase;
    public static ModConfigSpec.DoubleValue random_min_additional_decrease;
    public static ModConfigSpec.DoubleValue random_max_additional_decrease;

    public static ModConfigSpec.DoubleValue random_additional_increase_levelup_item_min;
    public static ModConfigSpec.DoubleValue random_additional_increase_levelup_item_max;

    public KillEnchantConfig(ModConfigSpec.Builder builder) {
        min_level = builder.defineInRange("min_level", 1, 1, 110);
        max_level = builder.defineInRange("max_level", 110, 1, 110);
        increase_per_kills = builder.define("increase_per_kills", 6);
        decrease_per_deaths = builder.define("decrease_per_kills", 6);
        enchant_levelup_item_increase = builder.define("enchant_levelup_item_increase", 6);

        random_min_additional_increase = builder.defineInRange("random_min_additional_increase.Must be lower than max_additional_increase.Must be decimal, 0 is always null", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        random_max_additional_increase = builder.defineInRange("random_max_additional_increase.Must be higher than mix_additional_increase.Must be decimal, 0 is always null", 0.01, Integer.MIN_VALUE, Integer.MAX_VALUE);
        random_min_additional_decrease = builder.defineInRange("random_min_additional_decrease.Must be lower than max_additional_decrease.Must be decimal, 0 is always null", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        random_max_additional_decrease = builder.defineInRange("random_max_additional_decrease.Must be higher than min_additional_decrease.Must be decimal, 0 is always null", 0.01, Integer.MIN_VALUE, Integer.MAX_VALUE);

        random_additional_increase_levelup_item_min = builder.defineInRange("random_additional_increase_levelup_item_min.Must be lower than increase_levelup_item_max.Must be decimal, 0 for null", 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        random_additional_increase_levelup_item_max = builder.defineInRange("random_additional_increase_levelup_item_max.Must be higher than increase_levelup_item_min.Must be decimal, 0 for null", 0.01, Integer.MIN_VALUE, Integer.MAX_VALUE);

        LOGGER.info("initialized config");
    }

    static {
        Pair<KillEnchantConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(KillEnchantConfig::new);

        SERVER = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
