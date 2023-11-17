package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MyRandomAttackAction extends AbstractGameAction {
    public MyRandomAttackAction(AbstractPlayer source, int amount) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.source = source;
        this.amount = amount;
    }

    @Override
    public void update() {
        target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target != null)
            addToTop(new DamageAction(target, new DamageInfo(source, amount, DamageInfo.DamageType.NORMAL)));

        this.isDone = true;
    }
}
