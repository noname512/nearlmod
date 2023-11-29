package nearlmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Iterator;

public class PureDamageAllEnemiesAction extends AbstractGameAction {
    private final int baseDamage;
    private final String name;

    public PureDamageAllEnemiesAction(AbstractCreature source, int baseDamage) {
        this(source, baseDamage, "");
    }

    public PureDamageAllEnemiesAction(AbstractCreature source, int baseDamage, AttackEffect effect) {
        this(source, baseDamage, "", effect);
    }

    public PureDamageAllEnemiesAction(AbstractCreature source, int baseDamage, String name) {
        this(source, baseDamage, name, AttackEffect.NONE);
    }

    public PureDamageAllEnemiesAction(AbstractCreature source, int baseDamage, String name, AttackEffect effect) {
        this(source, baseDamage, name, effect, DamageInfo.DamageType.NORMAL);
    }

    public PureDamageAllEnemiesAction(AbstractCreature source, int baseDamage, String name, AttackEffect effect, DamageInfo.DamageType type) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.source = source;
        this.baseDamage = baseDamage;
        this.damageType = type;
        this.name = name;
        attackEffect = effect;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            boolean playedMusic = false;
            for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!ms.isDeadOrEscaped()) {
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
            for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!ms.isDeadOrEscaped()) {
                    if (attackEffect == AttackEffect.POISON) {
                        ms.tint.color.set(Color.CHARTREUSE);
                        ms.tint.changeColor(Color.WHITE.cpy());
                    } else if (attackEffect == AttackEffect.FIRE) {
                        ms.tint.color.set(Color.RED);
                        ms.tint.changeColor(Color.WHITE.cpy());
                    }
                    DamageInfo info = new DamageInfo(source, baseDamage, damageType);
                    info.applyEnemyPowersOnly(ms);
                    info.name = this.name;
                    ms.damage(info);
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
