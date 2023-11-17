package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import nearlmod.monsters.CandleKnight;

public class ChangeImgAction extends AbstractGameAction {
    private String imgUrl;

    public ChangeImgAction(AbstractCreature source, String imgUrl) {
        actionType = ActionType.SPECIAL;
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
