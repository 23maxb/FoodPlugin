package max.plugin.foodPlugin2;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FoodPlugin2 extends JavaPlugin {

    @Override
    public void onEnable() {
        ItemStack SteakSandwich = new ItemStack(Material.BREAD, 1);
        ItemMeta old = SteakSandwich.getItemMeta();
        old.setDisplayName("Steak Sandwich");
        old.setLore(List.of("This sandwich has steak!", "Yummy!"));
        CustomModelDataComponent custom = old.getCustomModelDataComponent();

        List<String> a = new ArrayList<>(custom.getStrings());
        System.out.println(a);
        a.add("Steak Sandwich");
        custom.setStrings(a);
        old.setCustomModelDataComponent(custom);
        SteakSandwich.setItemMeta(old);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "SteakSandwich"),
                SteakSandwich);
        recipe.shape("B", "S", "B");
        recipe.setIngredient('S', Material.COOKED_BEEF);
        ItemStack bread = new ItemStack(Material.BREAD, 1);
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(bread));


        getServer().addRecipe(recipe);

/*
            if (GenerateTexturePack.generateRecipes() != null) {
                Arrays.stream(GenerateTexturePack.generateRecipes()).forEach(e -> {
                    getServer().addRecipe(e);
                });
            } else {
                System.out.println("Generate recipes failed");
            }*/
        getServer().getPluginManager().registerEvents(new FoodPluginListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
