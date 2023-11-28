package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;
import nearlmod.powers.LightPower;
import nearlmod.relics.UpgradedCoreCaster;

public class UseLightAction extends AbstractGameAction {
    public UseLightAction(AbstractCreature Target) {
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
        LightPower l = (LightPower) p.getPower("nearlmod:LightPower");
        if (l == null) {
            if (p.hasRelic(UpgradedCoreCaster.ID)) {
                l = new LightPower(p, 0);
                l.useLight(p, target);
            }
            isDone = true;
            return;
        }
        l.useLight(p, target);
        addToTop(new RemoveSpecificPowerAction(p, p, l));
        isDone = true;
    }
}
