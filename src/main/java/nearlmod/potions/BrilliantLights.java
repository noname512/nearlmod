package nearlmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import nearlmod.NLMOD;
import nearlmod.actions.ChooseSpecificCardAction;
import nearlmod.patches.NearlTags;

import java.util.ArrayList;
import java.util.Collections;

public class BrilliantLights extends AbstractPotion {
    public static String ID = "nearlmod:BrilliantLights";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public BrilliantLights() {
        super(NAME, ID, PotionRarity.COMMON, PotionSize.CARD, PotionColor.ENERGY);
        this.labOutlineColor = NLMOD.NearlGold;
        this.description = potionStrings.DESCRIPTIONS[0];
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:light")), BaseMod.getKeywordDescription("nearlmod:light")));
    }
    @Override
    public void use(AbstractCreature abstractCreature) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.srcCommonCardPool.group)
            if (c.hasTag(NearlTags.IS_GAIN_LIGHT) || c.hasTag(NearlTags.IS_USE_LIGHT))
                list.add(c);
        for (AbstractCard c : AbstractDungeon.srcUncommonCardPool.group)
            if (c.hasTag(NearlTags.IS_GAIN_LIGHT) || c.hasTag(NearlTags.IS_USE_LIGHT))
                list.add(c);
        for (AbstractCard c : AbstractDungeon.srcRareCardPool.group)
            if (c.hasTag(NearlTags.IS_GAIN_LIGHT) || c.hasTag(NearlTags.IS_USE_LIGHT))
                list.add(c);
        Collections.shuffle(list);
        ArrayList<AbstractCard> chooseList = new ArrayList<>(list.subList(0, 3));
        addToBot(new ChooseSpecificCardAction(chooseList, true));
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BrilliantLights();
    }
}
