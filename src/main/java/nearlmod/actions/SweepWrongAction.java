package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import nearlmod.cards.AbstractNearlCard;

public class SweepWrongAction extends AbstractGameAction {
    private final int baseDamage;
    private final int extraDamage;

    public SweepWrongAction(AbstractCreature source, int baseDamage, int extraDamage) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        startDuration = duration;
        this.source = source;
        this.baseDamage = baseDamage;
        this.extraDamage = extraDamage;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            boolean playedMusic = false;
            for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!ms.isDeadOrEscaped()) {
                    if (ms.getIntentBaseDmg() >= 0) {
                        attackEffect = AttackEffect.BLUNT_HEAVY;
                    } else {
                        attackEffect = AttackEffect.BLUNT_LIGHT;
                    }
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(ms.hb.cX, ms.hb.cY, attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(ms.hb.cX, ms.hb.cY, attackEffect));
                    }
                }
            }
        }

        this.tickDuration();
        if (this.isDone) {
            DamageInfo info;
            for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!ms.isDeadOrEscaped()) {
                    if (ms.getIntentBaseDmg() >= 0) {
                        info = new DamageInfo(source, AbstractNearlCard.staticCalcDmg(ms, baseDamage + extraDamage, DamageInfo.DamageType.NORMAL, false));
                    } else {
                        info = new DamageInfo(source, AbstractNearlCard.staticCalcDmg(ms, baseDamage, DamageInfo.DamageType.NORMAL, false));
                    }
                    ms.damage(info);
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
        }
    }
}
