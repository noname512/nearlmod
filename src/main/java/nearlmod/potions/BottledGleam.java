package nearlmod.potions;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.NLMOD;

public class BottledGleam extends AbstractPotion {
    public static String ID = "nearlmod:BottledGleam";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public BottledGleam() {
        super(NAME, ID, PotionRarity.RARE, PotionSize.FAIRY, PotionColor.FAIRY);
        this.labOutlineColor = NLMOD.NearlGold;
        this.description = potionStrings.DESCRIPTIONS[0];
        this.isThrown = true;
        this.targetRequired = true;
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]), GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0])));
    }

    @Override
    public void use(AbstractCreature m) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -99)));
        if (m != null && !m.hasPower("Artifact")) {
            addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, 99)));
        }
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottledGleam();
    }

    @SpirePatch(clz = AbstractPotion.class, method = "initializeImage")
    public static class SpotImagePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPotion __instance, @ByRef Texture[] ___spotsImg) {
            if (__instance instanceof BottledGleam)
                ___spotsImg[0] = new Texture("images/ui/gleam_spots.png");
        }
    }
}
