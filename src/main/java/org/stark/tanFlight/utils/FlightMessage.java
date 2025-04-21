package org.stark.tanFlight.utils;

import org.bukkit.ChatColor;
import org.stark.tanFlight.TanFlight;

import javax.annotation.Nullable;
import java.util.Map;

public class FlightMessage {

    public static String getMessage(String key) {
        return getMessage(key, null);
    }

    public static String getMessage(String key, @Nullable Map<String, String> params) {
        // Получаем строку из конфига
        String message = TanFlight.getInstance().getConfig().getString("messages." + key, "Message not found!");
        String prefix = TanFlight.getInstance().getConfig().getString("prefix", "Prefix not found!");

        // Если параметры переданы, заменяем их в сообщении
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // Заменяем {key} на значение из params
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        // Преобразуем цвета из символов (&a, &7 и т. д.) в правильный формат
        message = ChatColor.translateAlternateColorCodes('&', prefix + message);

        return message;
    }
}
