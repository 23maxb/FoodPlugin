package max.plugin.foodPlugin2;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FoodPluginListener implements org.bukkit.event.Listener {
    public static final String resourcePackURL = "temp";

    @EventHandler
    public void giveResourcePack(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setResourcePack(resourcePackURL);

    }

    @EventHandler
    public void resourcePackResult(PlayerResourcePackStatusEvent event) {
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            Player player = event.getPlayer();
            player.sendMessage("Your client did not accept the resource pack. If you want to download it later, relog or use this link: ");
            player.sendMessage(resourcePackURL);
        }
    }

    @EventHandler
    public void onPlayerConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the item is your custom steak
        if (item.getItemMeta() != null &&
                item.getItemMeta().getCustomModelDataComponent() != null &&
                item.getItemMeta().getCustomModelDataComponent().getStrings().contains("Steak Sandwich")) {

            // Cancel the default consumption effect
            event.setCancelled(true);

            // Set custom hunger and saturation values
            int customHunger = 20; // Example: restores 8 hunger points
            float customSaturation = 20.0f; // Example: restores 4 saturation points

            // Add hunger and saturation to the player
            player.setFoodLevel(Math.min(20, player.getFoodLevel() + customHunger));
            player.setSaturation(Math.min(20.0f, player.getSaturation() + customSaturation));

            // Send a message to the player
            player.sendMessage("You ate a Steak Sandwich and feel satisfied!");
        }
    }
}
