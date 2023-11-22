package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AbstractNearlCard;

public class MyRandomAttackAction extends AbstractGameAction {
    public final DamageInfo info;
    public MyRandomAttackAction(DamageInfo info) {
        actionType = ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_FAST;
        this.info = info;
    }

    @Override
    public void update() {
        AbstractMonster ms = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (ms != null) {
            DamageInfo actualInfo = new DamageInfo(info.owner, AbstractNearlCard.staticCalcDmg(ms, info.base, DamageInfo.DamageType.NORMAL));
            addToTop(new DamageAction(ms, actualInfo));
        }

        this.isDone = true;
    }
}
