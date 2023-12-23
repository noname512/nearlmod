package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.monsters.BloodBlade;
import nearlmod.patches.CurseRelicPatch;

public class BloodEntangle extends CustomRelic {

    public static final String ID = "nearlmod:BloodEntangle";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/cureup.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/cureup_p.png");
    public BloodEntangle() {
        super(ID, IMG, IMG_OUTLINE, CurseRelicPatch.CURSE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void atBattleStart() {
        float leftX = 100.0F;
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            leftX = Math.min(leftX, (ms.drawX - Settings.WIDTH * 0.75F) / Settings.xScale);
        addToTop(new SpawnMonsterAction(new BloodBlade(leftX - 100.0F, 0.0F), false));
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return new BloodEntangle();
    }
}
