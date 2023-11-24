package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import nearlmod.powers.LightPower;
import nearlmod.powers.ShadowPower;

public class KnightCompetitionAction extends AbstractGameAction {
    private final int increaseGold;
    public KnightCompetitionAction(int increaseGold) {
        this.increaseGold = increaseGold;
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FASTER;
    }

    @Override
    public void update() {
        AbstractMonster ms = AbstractDungeon.getRandomMonster();
        if (ms == null) {
            isDone = true;
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower power = p.getPower("nearlmod:LightPower");
        if (power == null) {
            isDone = true;
            return;
        }
        amount = power.amount;
        ms.damage(new DamageInfo(p, amount, DamageInfo.DamageType.THORNS));
        if (p.getPower("nearlmod:Poem'sLooks") != null) {
            addToTop(new ApplyPowerAction(p, p, new ShadowPower(p, amount)));
        }
        LightPower.amountForBattle += amount;
        addToTop(new RemoveSpecificPowerAction(p, p, power));
        if ((ms.isDying || ms.currentHealth <= 0) && !ms.halfDead && !ms.hasPower("Minion")) {
            p.gainGold(increaseGold);
            for (int i = 0; i < increaseGold; i++) {
                AbstractDungeon.effectList.add(new GainPennyEffect(p, ms.hb.cX, ms.hb.cY, p.hb.cX, p.hb.cY, true));
            }
        }

        if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
        isDone = true;
    }
}
