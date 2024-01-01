package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class TempStrengthAction extends AbstractGameAction {
    public TempStrengthAction(AbstractCreature s, AbstractCreature p, int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        source = s;
        target = p;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (target.hasPower("nearlmod:Exsanguination")) {
            int current = 0;
            if (target.hasPower("Strength")) {
                current = target.getPower("Strength").amount;
            }
            if (amount > -current)
            {
                target.getPower("nearlmod:Exsanguination").flash();
                amount = -current;
            }
        }
        if (amount == 0) {
            isDone = true;
            return;
        }
        addToTop(new ApplyPowerAction(target, source, new StrengthPower(target, amount)));
        addToTop(new ApplyPowerAction(target, source, new LoseStrengthPower(target, amount)));
        isDone = true;
    }
}
