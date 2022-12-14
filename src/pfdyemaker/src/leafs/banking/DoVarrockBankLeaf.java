package pfdyemaker.src.leafs.banking;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Sleep;
import pfdyemaker.src.data.DyeMakerConfig;
import pfdyemaker.src.framework.Leaf;

public class DoVarrockBankLeaf extends Leaf {

    DyeMakerConfig config = DyeMakerConfig.getDyeMakerConfig();

    @Override
    public boolean isValid() {
        return BankLocation.VARROCK_EAST.getArea(1).contains(Players.getLocal()) && Inventory.contains("Redberries");
    }

    @Override
    public int onLoop() {
        if (!Bank.isOpen()) {
            DyeMakerConfig.status = "Opening bank";
            Bank.open();
            Sleep.sleepUntil(Bank::isOpen, 2000);
        }

        if (Bank.isOpen()) {
            DyeMakerConfig.status = "Deposit redberries";
            Bank.depositAllItems();
            config.setIngredientsCollected(config.getIngredientsCollected() + Inventory.fullSlotCount());
            Sleep.sleepUntil(Inventory::isEmpty, 2000);
            Bank.close();
        }
        return 1000;
    }
}
