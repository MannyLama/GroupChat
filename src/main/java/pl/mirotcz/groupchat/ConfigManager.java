package pl.mirotcz.groupchat;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
	
	private final String fileName;
	private final JavaPlugin plugin;
	
	private File configFile;
    private FileConfiguration fileConfiguration;
    
    public ConfigManager(JavaPlugin plugin, String fileName) {
    	if (plugin == null)
            throw new IllegalArgumentException("plugin cannot be null");
    	this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null)
            throw new IllegalStateException();
        configFile = new File(plugin.getDataFolder(), fileName);
    }
        
public void reloadConfig() {
	fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
	
	InputStream defConfigStream = plugin.getResource(fileName);
	Reader defConfigReader = new InputStreamReader(defConfigStream);
    if (defConfigReader != null) {
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigReader);
        fileConfiguration.setDefaults(defConfig);
    }
}

public FileConfiguration getConfig() {
    if (fileConfiguration == null) {
        reloadConfig();
    }
    return fileConfiguration;
}

public void saveConfig() {
    if (fileConfiguration == null || configFile == null) {
        return;
    }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
}

public void saveDefaultConfig() {
    if (!configFile.exists()) {            
        plugin.saveResource(fileName, false);
    }
}


}
