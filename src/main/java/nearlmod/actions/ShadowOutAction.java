package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.Viviana;
import nearlmod.vfx.ShadowOutEffect;

public class ShadowOutAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final int damage;

    public ShadowOutAction(AbstractPlayer p, int damage, int amount) {
        actionType = ActionType.DAMAGE;
        if (Settings.FAST_MODE) duration = 0.6F;
        else duration = 1.2F;
        startDuration = duration;
        this.p = p;
        this.damage = damage;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            AbstractDungeon.effectList.add(new ShadowOutEffect());
        }

        this.tickDuration();
        if (this.isDone) {
            addToTop(new PureDamageAllEnemiesAction(p, damage, Viviana.ORB_ID + AbstractFriendCard.damageSuffix, AttackEffect.FIRE));
            for (int i = 1; i < amount; i++)
                addToTop(new PureDamageAllEnemiesAction(p, damage, Viviana.ORB_ID + AbstractFriendCard.damageSuffix, AttackEffect.NONE, true));
            addToTop(new UseShadowAction(p));

            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
        }
    }
}
