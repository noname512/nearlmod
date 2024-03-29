package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import nearlmod.cards.FirstAid;

public class FirstAidMode extends CustomRelic {

    public static final String ID = "nearlmod:FirstAidMode";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/firstaidmode.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/firstaidmode_p.png");
    public FirstAidMode() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractCard c = new FirstAid();
        c.exhaust = true;
        c.selfRetain = true;
        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c, Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FirstAidMode();
    }
}
