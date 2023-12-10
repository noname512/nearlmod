package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.Dawn;

import java.util.ArrayList;

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
                DamageInfo actualInfo = new DamageInfo(info.owner, AbstractNearlCard.staticCalcDmg(mo, info.base, DamageInfo.DamageType.NORMAL, false));
                mo.damage(actualInfo);
                if ((mo.isDying || mo.currentHealth <= 0) && !mo.halfDead) {
                    canUpgrade = false;
                }
            }
        }
        if (canUpgrade) {
            sourceCard.upgrade();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
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
