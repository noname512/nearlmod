package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import nearlmod.powers.LightPower;

import java.util.ArrayList;

public class WeakenAllAction extends AbstractGameAction {

    private AbstractPlayer source;
    private int amount;

    public WeakenAllAction(AbstractPlayer source, int amount) {
        actionType = ActionType.POWER;
        duration = Settings.ACTION_DUR_FAST;
        this.source = source;
        this.amount = amount;
    }

    public WeakenAllAction(AbstractPlayer source) {
        this(source, 1);
    }

    @Override
    public void update() {
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for (AbstractMonster ms : monsters) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(ms, source, new WeakPower(ms, amount, false)));
        }
        isDone = true;
    }
}
