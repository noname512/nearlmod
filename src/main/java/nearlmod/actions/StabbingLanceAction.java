package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.util.CostReserves;

public class StabbingLanceAction extends AbstractGameAction {
    public int costInc;
    public StabbingLanceAction(int amount, int costInc) {
        this.actionType = ActionType.SPECIAL;
        this.amount = amount;
        this.costInc = costInc;
    }

    @Override
    public void update() {
        int resMonster = 0;
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (!ms.isDeadOrEscaped())
                resMonster++;
        if (resMonster < amount)
            addToBot(new GainCostAction(costInc * (amount - resMonster)));
        this.isDone = true;
    }
}
