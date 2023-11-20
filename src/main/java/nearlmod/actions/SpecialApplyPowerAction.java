package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import java.util.Collections;

public class SpecialApplyPowerAction extends AbstractGameAction {
    private final AbstractPower powerToApply;
    private float startingDuration;

    public SpecialApplyPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount) {
        if (Settings.FAST_MODE) {
            startingDuration = 0.1F;
        } else {
            startingDuration = Settings.ACTION_DUR_FAST;
        }

        setValues(target, source, stackAmount);
        duration = startingDuration;
        this.powerToApply = powerToApply;
        actionType = ActionType.POWER;
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            duration = 0.0F;
            startingDuration = 0.0F;
            isDone = true;
        }
    }

    public void update() {
        if (target != null && !target.isDeadOrEscaped()) {
            if (duration == startingDuration) {
                if (source != null) {
                    for (AbstractPower pow : source.powers)
                        pow.onApplyPower(powerToApply, target, source);
                }
                boolean hasBuffAlready = false;
                for (AbstractPower pow : target.powers)
                    if (pow.ID.equals(powerToApply.ID)) {
                        if (pow.ID.equals(StrengthPower.POWER_ID) || pow.ID.equals(DexterityPower.POWER_ID)) {
                            if (amount > 0)
                                pow.flash();
                            pow.amount += amount;
                            if (pow.amount == 0) {
                                target.powers.remove(pow);
                            } else if (pow.amount > 999) {
                                pow.amount = 999;
                            } else if (pow.amount < -999) {
                                pow.amount = -999;
                            }
                        } else {
                            pow.stackPower(amount);
                            pow.flash();
                        }
                        pow.updateDescription();
                        hasBuffAlready = true;
                        break;
                    }
                if (!hasBuffAlready) {
                    target.powers.add(powerToApply);
                    Collections.sort(target.powers);
                    powerToApply.flash();
                }
                AbstractDungeon.onModifyPower();
            }

            tickDuration();
        } else {
            isDone = true;
        }
    }
}
