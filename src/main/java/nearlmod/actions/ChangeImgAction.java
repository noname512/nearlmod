package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import nearlmod.monsters.CandleKnight;

import java.util.ArrayList;

public class ChangeImgAction extends AbstractGameAction {

    private AbstractCreature source;
    private String imgUrl;

    public ChangeImgAction(AbstractCreature source, String imgUrl) {
        actionType = ActionType.POWER;
        duration = Settings.ACTION_DUR_FAST;
        this.source = source;
        this.imgUrl = imgUrl;
    }

    @Override
    public void update() {
        if (source instanceof CandleKnight)
            ((CandleKnight)source).changeImg(imgUrl);
        isDone = true;
    }
}
