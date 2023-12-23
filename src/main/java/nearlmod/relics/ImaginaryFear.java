package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.patches.CurseRelicPatch;

public class ImaginaryFear extends CustomRelic {

    public static final String ID = "nearlmod:ImaginaryFear";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/imaginaryfear.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/imaginaryfear_p.png");
    public boolean isFirstTurn;
    public ImaginaryFear() {
        super(ID, IMG, IMG_OUTLINE, CurseRelicPatch.CURSE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void atBattleStart() {
        isFirstTurn = true;
    }
    
    @Override
    public void onPlayerEndTurn() {
        if (isFirstTurn) {
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 8, DamageInfo.DamageType.THORNS));
            isFirstTurn = false;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ImaginaryFear();
    }
}
