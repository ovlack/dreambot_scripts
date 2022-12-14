package pfdyemaker.src.leafs.banking;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import pfdyemaker.src.data.DyeMakerConfig;
import pfdyemaker.src.framework.Leaf;

public class DoDraynorBankLeaf extends Leaf {

    DyeMakerConfig config = DyeMakerConfig.getDyeMakerConfig();

    @Override
    public boolean isValid() {
        return BankLocation.DRAYNOR.getArea(1).contains(Players.getLocal());
    }

    @Override
    public int onLoop() {
        if (!Bank.isOpen()) {
            config.setStatus("Opening bank");
            Bank.open();
            Sleep.sleepUntil(Bank::isOpen, 2000);
        }

        if (Bank.isOpen()) {
            config.setStatus("Depositing dye");
            Bank.depositAllExcept("Coins", config.getDyeIngredient());
            Logger.log("deposited items");
            Sleep.sleepUntil(() -> !Inventory.contains(" dye"), 2000);
        }

        //todo add separate bank handling for different dyes
        if (Bank.isOpen() && !Inventory.contains(config.getDyeIngredient())) {
            config.setStatus("Withdrawing " + config.getDyeIngredient());
            if (Bank.contains(config.getDyeIngredient())) {
                Bank.withdrawAll(config.getDyeIngredient());
                Logger.log("withdrew ingredient");
                Sleep.sleepUntil(() -> Inventory.contains(config.getDyeIngredient()), 2000);
            }
        }

      if (!Inventory.contains(config.getDyeIngredient())) {
          Logger.log("out of " + config.getDyeIngredient());
          ScriptManager.getScriptManager().stop();
      }
        return 1000;
    }
}
