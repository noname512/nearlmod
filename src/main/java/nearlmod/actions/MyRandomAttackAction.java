package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MyRandomAttackAction extends AbstractGameAction {
    public final DamageInfo info;
    public MyRandomAttackAction(DamageInfo info) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.info = info;
    }

    @Override
    public void update() {
        target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        // TODO
        if (target != null)
            addToTop(new DamageAction(target, info));

        this.isDone = true;
    }
}
