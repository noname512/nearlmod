package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nearlmod.orbs.Horn;

public class FallingShieldAction extends AbstractGameAction {
    private final int cost;
    private final int times;
    private final boolean summon;
    public FallingShieldAction(int cost, int amount, int times, boolean summon, AbstractCreature target) {
        this.actionType = ActionType.DAMAGE;
        this.amount = amount;
        this.cost = cost;
        this.times = times;
        this.summon = summon;
        this.target = target;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            isDone = true;
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        for (int i = 0; i < times; i++) {
            addToBot(new DamageAction(target, new DamageInfo(p, amount)));
        }
        if (summon) {
            addToTop(new SummonFriendAction(new Horn()));
        }
        if (cost > 0) {
            addToBot(new LoseHPAction(p, p, cost));
        }
        isDone = true;
    }
}
