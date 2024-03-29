package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.patches.CurseRelicPatch;
import nearlmod.powers.SuperWeakPower;

public class Revenge extends CustomRelic {

    public static final String ID = "nearlmod:Revenge";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/revenge.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/revenge_p.png");
    public Revenge() {
        super(ID, IMG, IMG_OUTLINE, CurseRelicPatch.CURSE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new SuperWeakPower(p, 2)));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Revenge();
    }
}
