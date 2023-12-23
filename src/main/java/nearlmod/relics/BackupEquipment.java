package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.cards.special.StartWithAtkStance;
import nearlmod.cards.special.StartWithDefStance;

import java.util.ArrayList;

public class BackupEquipment extends CustomRelic {

    public static final String ID = "nearlmod:BackupEquipment";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/backupequipment.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/backupequipment_p.png");
    public BackupEquipment() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new StartWithAtkStance());
        list.add(new StartWithDefStance());
        addToTop(new ChooseOneAction(list));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BackupEquipment();
    }
}
