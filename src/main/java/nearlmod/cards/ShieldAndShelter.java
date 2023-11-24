package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.GainCostAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;

public class ShieldAndShelter extends AbstractNearlCard {
    public static final String ID = "nearlmod:ShieldAndShelter";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = -1;
    private static final int BLOCK_AMT = 3;
    private static final int LIGHT_AMT = 3;
    private static final int COST_AMT = 1;

    public ShieldAndShelter() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        tags.add(NearlTags.IS_GAIN_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = energyOnUse;
        if (upgraded)
            amount++;
        addToBot(new GainBlockAction(p, amount * BLOCK_AMT));
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, amount * LIGHT_AMT)));
        addToBot(new GainCostAction(amount * COST_AMT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShieldAndShelter();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
