package de.greenman999.fullbright.compat;
//? if fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.greenman999.fullbright.gui.ConfigScreen;
import gg.essential.universal.UScreen;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> (UScreen) new ConfigScreen(parent);
    }
}
//?}