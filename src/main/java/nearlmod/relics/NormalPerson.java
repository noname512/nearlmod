package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.patches.CurseRelicPatch;

public class NormalPerson extends CustomRelic {

    public static final String ID = "nearlmod:NormalPerson";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/normalperson.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/normalperson_p.png");
    public NormalPerson() {
        super(ID, IMG, IMG_OUTLINE, CurseRelicPatch.CURSE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NormalPerson();
    }
}
