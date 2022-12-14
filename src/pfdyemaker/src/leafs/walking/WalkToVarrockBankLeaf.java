package pfdyemaker.src.leafs.walking;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Sleep;
import pfdyemaker.src.data.DyeMakerConfig;
import pfdyemaker.src.framework.Leaf;

public class WalkToVarrockBankLeaf extends Leaf {

    @Override
    public boolean isValid() {
        return Inventory.contains("Redberries") && Inventory.isFull() && !BankLocation.VARROCK_EAST.getArea(1).contains(Players.getLocal());
    }

    @Override
    public int onLoop() {
        if (Walking.shouldWalk()) {
            DyeMakerConfig.status = "Walking to bank";
            Walking.walk(BankLocation.VARROCK_EAST.getArea(1).getCenter());
            Sleep.sleepUntil(() -> BankLocation.VARROCK_EAST.getArea(3).contains(Players.getLocal()), 1000);
        }
        return 600;
    }
}
