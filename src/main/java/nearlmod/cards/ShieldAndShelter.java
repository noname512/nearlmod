package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
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
    public static final String IMG_PATH = "images/cards/shieldandshelter.png";
    private static final int COST = -1;
    private static final int BLOCK_AMT = 3;
    private static final int LIGHT_AMT = 3;
    private static final int COST_AMT = 1;

    public ShieldAndShelter() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(NearlTags.IS_GAIN_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = energyOnUse;
        if (upgraded)
            amount++;
        if (p.hasRelic(ChemicalX.ID)) {
            amount += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (amount > 0) {
            for (int i = 1; i <= amount; i++) {
                addToBot(new GainBlockAction(p, block));
                addToBot(new ApplyPowerAction(p, p, new LightPower(p, LIGHT_AMT)));
                addToBot(new GainCostAction(COST_AMT));
            }
            if (!freeToPlayOnce)
                p.energy.use(EnergyPanel.totalCount);
        }
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
