package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nearlmod.powers.ShadowPower;

public class UseShadowAction extends AbstractGameAction {
    public UseShadowAction(AbstractCreature Target) {
        actionType = ActionType.POWER;
        duration = Settings.ACTION_DUR_FAST;
        target = Target;
    }
    @Override
    public void update() {
        if (target == null) {
            isDone = true;
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        ShadowPower l = (ShadowPower) p.getPower("nearlmod:Shadow");
        if (l == null) {
            isDone = true;
            return;
        }
        l.useShadow(p, target);
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p, p, l));
        isDone = true;
    }
}
