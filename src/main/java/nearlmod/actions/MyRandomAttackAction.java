package nearlmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

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
        if (target != null) {
            float tmp = info.base;
            for (AbstractPower power : target.powers)
                tmp = power.atDamageReceive(tmp, info.type);
            for (AbstractPower power : target.powers)
                tmp = power.atDamageFinalReceive(tmp, info.type);
            if (tmp < 0.0F) tmp = 0.0F;
            DamageInfo actualInfo = new DamageInfo(info.owner, MathUtils.floor(tmp));
            addToTop(new DamageAction(target, actualInfo));
        }

        this.isDone = true;
    }
}
