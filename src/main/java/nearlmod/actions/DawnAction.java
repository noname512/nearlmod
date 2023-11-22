package nearlmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import nearlmod.cards.Dawn;

import java.util.ArrayList;
import java.util.Iterator;

public class DawnAction extends AbstractGameAction {

    private final DamageInfo info;
    private final Dawn sourceCard;

    public DawnAction(DamageInfo info, Dawn card) {
        actionType = ActionType.POWER;
        duration = Settings.ACTION_DUR_FAST;
        this.info = info;
        sourceCard = card;
    }

    @Override
    public void update() {
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        boolean canUpgrade = true;
        for (AbstractMonster mo : monsters) {
            if (!mo.isDeadOrEscaped()) {
                float tmp = info.base;
                for (AbstractPower power : mo.powers)
                    tmp = power.atDamageReceive(tmp, info.type);
                for (AbstractPower power : mo.powers)
                    tmp = power.atDamageFinalReceive(tmp, info.type);
                if (tmp < 0.0F)
                    tmp = 0.0F;
                DamageInfo actualInfo = new DamageInfo(info.owner, MathUtils.floor(tmp));
                mo.damage(actualInfo);
                if ((mo.isDying || mo.currentHealth <= 0) && !mo.halfDead) {
                    canUpgrade = false;
                }
            }
        }
        if (canUpgrade) {
            sourceCard.upgrade();
            Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();

            AbstractCard c;
            while(var1.hasNext()) {
                c = (AbstractCard)var1.next();
                if (c == sourceCard.copiedFrom) {
                    sourceCard.copiedFrom.upgrade();
                    // Copied from 勤学精进
                    AbstractDungeon.player.bottledCardUpgradeCheck(this.sourceCard.copiedFrom);
                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(this.sourceCard.copiedFrom.makeStatEquivalentCopy()));
                    this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                }
            }
        }
        isDone = true;
    }
}
