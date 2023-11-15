package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import nearlmod.util.CostReserves;

public class GainCostAction extends AbstractGameAction {
    public GainCostAction(int amount) {
        this.actionType = ActionType.SPECIAL;
        this.amount = amount;
    }

    @Override
    public void update() {
        CostReserves.addReserves(amount);
        this.isDone = true;
    }
}
