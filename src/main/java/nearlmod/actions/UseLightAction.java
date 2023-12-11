package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.powers.LightPower;
import nearlmod.relics.UpgradedCoreCaster;

public class UseLightAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final AbstractMonster m;
    public UseLightAction(AbstractPlayer p, AbstractMonster m) {
        actionType = ActionType.POWER;
        duration = Settings.ACTION_DUR_FAST;
        this.p = p;
        this.m = m;
    }
    @Override
    public void update() {
        if (p == null) {
            isDone = true;
            return;
        }
        LightPower l = (LightPower) p.getPower("nearlmod:LightPower");
        if (l == null) {
            if (p.hasRelic(UpgradedCoreCaster.ID)) {
                l = new LightPower(p, 0);
                l.useLight(p, m);
            }
            isDone = true;
            return;
        }
        l.useLight(p, m);
        addToTop(new RemoveSpecificPowerAction(p, p, l));
        isDone = true;
    }
}
